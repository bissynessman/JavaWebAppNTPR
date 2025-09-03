package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.comparator.GradeComparator;
import tvz.ntpr.core.config.AppProperties;
import tvz.ntpr.core.entity.*;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.helper.ReportWrapper;
import tvz.ntpr.core.rest.DatabaseApi;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static tvz.ntpr.core.utils.DigitalSignature.createDetachedSignature;
import static tvz.ntpr.core.utils.DigitalSignature.verifySignature;
import static tvz.ntpr.core.utils.HtmlToPdf.*;
import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.config.Urls.*;

@Controller
@RequestMapping(URL_STUDENT)
@SessionAttributes("userLogin")
public class StudentController {
    private static final String NTPR_PROTOCOL_PREFIX = "ntpr://download?url=";

    private final StudentService studentService;
    private final GradeService gradeService;
    private final CourseService courseService;
    private final AuthenticationService authenticationService;
    private final ReportService reportService;
    private final AssignmentService assignmentService;
    private final Messages messages;
    private final AppProperties appProperties;

    public StudentController(StudentService studentService,
                             GradeService gradeService,
                             CourseService courseService,
                             AuthenticationService authenticationService,
                             ReportService reportService,
                             AssignmentService assignmentService,
                             Messages messages,
                             AppProperties appProperties) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.courseService = courseService;
        this.authenticationService = authenticationService;
        this.reportService = reportService;
        this.assignmentService = assignmentService;
        this.messages = messages;
        this.appProperties = appProperties;
    }

    @GetMapping
    public String showStudentView(Model model) {
        authenticationService.refresh();
        initModel(model);
        return "student";
    }

    @GetMapping(URL_DOWNLOAD)
    public String saveStudentReport(Model model, RedirectAttributes redirectAttributes) {
        authenticationService.refresh();
        User user = (User) model.getAttribute("userLogin");
        String studentId = user.getUserUuid();

        try {
            File data = scrapeHtmlToPdf(appProperties.getApplicationUrl() + URL_STUDENT + "/" + studentId);
            File signature = createDetachedSignature(data);

            if (verifySignature(data, signature) == 0) {
                Report report = Report.builder()
                        .fileName(data.getName())
                        .pathToFile(data.toPath())
                        .pathToSignature(signature.toPath())
                        .student(studentId)
                        .build();

                reportService.saveReport(new ReportWrapper(report, data, signature));
                String downloadUrl =
                        NTPR_PROTOCOL_PREFIX + DatabaseApi.REPORTS_API + "/" + report.getStudent();
                redirectAttributes.addFlashAttribute("downloadUrl", downloadUrl);
                redirectAttributes.addFlashAttribute("success", messages.getMessage("success.generation"));
            } else {
                Files.deleteIfExists(data.toPath());
                Files.deleteIfExists(signature.toPath());
                redirectAttributes.addFlashAttribute("error", messages.getMessage("error.generation-failed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:" + URL_STUDENT;
    }

    @GetMapping(URL_STUDENT_ID)
    public String showStudentView(Model model, @PathVariable("studentId") String studentId) {
        initialize(model, URL_STUDENT + "/" + studentId);
        Student student = studentService.getStudentById(studentId);
        List<Grade> grades = gradeService.getGradesByStudent(student.getId());
        for (Grade grade : grades)
            grade.setCourseName(courseService.getCourseById(grade.getCourse()).getName());
        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        return "student";
    }

    private void initModel(Model model) {
        initialize(model, URL_STUDENT);
        User userLogin = (User) model.getAttribute("userLogin");
        Student student = studentService.getStudentById(userLogin.getUserUuid());
        List<Grade> grades = gradeService.getGradesByStudent(student.getId()).stream()
                .sorted(new GradeComparator())
                .toList();
        for (Grade grade : grades)
            grade.setCourseName(courseService.getCourseById(grade.getCourse()).getName());
        List<Assignment> assignments = assignmentService.getActiveForStudent(student.getId());
        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        if (!assignments.isEmpty())
            model.addAttribute("assignments", assignments);
    }
}