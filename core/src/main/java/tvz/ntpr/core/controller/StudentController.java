package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.entity.Report;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.entity.Grade;
import tvz.ntpr.core.entity.Student;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.helper.ReportWrapper;
import tvz.ntpr.core.rest.DatabaseApi;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.CourseService;
import tvz.ntpr.core.service.GradeService;
import tvz.ntpr.core.service.ReportService;
import tvz.ntpr.core.service.StudentService;

import java.io.File;
import java.util.List;

import static tvz.ntpr.core.utils.DigitalSignature.createDetachedSignature;
import static tvz.ntpr.core.utils.HtmlToPdf.*;
import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.config.Urls.*;

@Controller
@RequestMapping(URL_STUDENT)
@SessionAttributes({ "userLogin", "downloadUrl" })
public class StudentController {
    private static final String NTPR_PROTOCOL_PREFIX = "ntpr://download?url=";

    private final StudentService studentService;
    private final GradeService gradeService;
    private final CourseService courseService;
    private final AuthenticationService authenticationService;
    private final ReportService reportService;
    private final Messages messages;

    public StudentController(StudentService studentService,
                             GradeService gradeService,
                             CourseService courseService,
                             AuthenticationService authenticationService,
                             ReportService reportService,
                             Messages messages) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.courseService = courseService;
        this.authenticationService = authenticationService;
        this.reportService = reportService;
        this.messages = messages;
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

        try {
            File data = scrapeHtmlToPdfFile(BASE_URL + URL_STUDENT, user.getUserUuid());
            File signature = createDetachedSignature(data);

            Report report = Report.builder()
                    .fileName(data.getName())
                    .pathToFile(data.toPath())
                    .pathToSignature(signature.toPath())
                    .student(user.getUserUuid())
                    .build();

            reportService.saveReport(new ReportWrapper(report, data, signature));
            String downloadUrl = NTPR_PROTOCOL_PREFIX + DatabaseApi.REPORTS_API + "/" + report.getStudent();
            model.addAttribute("downloadUrl", downloadUrl);
            return "redirect:" + URL_STUDENT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("success", messages.getMessage("success.download"));
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
        List<Grade> grades = gradeService.getGradesByStudent(student.getId());
        for (Grade grade : grades)
            grade.setCourseName(courseService.getCourseById(grade.getCourse()).getName());
        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
    }
}