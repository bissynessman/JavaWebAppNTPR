package tvz.ntpr.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.entity.Grade;
import tvz.ntpr.core.entity.Student;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.CourseService;
import tvz.ntpr.core.service.GradeService;
import tvz.ntpr.core.service.StudentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static tvz.ntpr.core.utils.DigitalSignature.sign;
import static tvz.ntpr.core.utils.HtmlToPdf.*;
import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.Urls.*;

@Controller
@RequestMapping(URL_STUDENT)
@AllArgsConstructor
@SessionAttributes("userLogin")
public class StudentController {
    @Autowired
    private final StudentService studentService;
    @Autowired
    private final GradeService gradeService;
    @Autowired
    private final CourseService courseService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;

    @GetMapping
    public String showStudentView(Model model) {
        authenticationService.refresh();
        initModel(model);
        return "student";
    }

    @GetMapping("/download")
    public String saveStudentReport(Model model, RedirectAttributes redirectAttributes) {
        authenticationService.refresh();
        User user = (User) model.getAttribute("userLogin");

        Path studentReportsDirectory = Paths.get("target", "generated", "student_reports");
        File tmpFile = Paths.get(studentReportsDirectory.toString(), "student_report.tmp.pdf").toFile();
        try {
            Files.createDirectories(studentReportsDirectory);
            scrapeHtmlToPdfFile(BASE_URL + URL_STUDENT, user.getUserUuid(), tmpFile);
            File outputFile = sign(tmpFile);
            Files.deleteIfExists(tmpFile.toPath());
            // TODO: expose file to download endpoint; call ntpr protocol
            // maybe store file in database and download using uuid? - uses blob ;)
        } catch (IOException e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("success", messages.getMessage("success.download"));
        return "redirect:" + URL_STUDENT;
    }

    @GetMapping("/{studentId}")
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