package tvz.ntpr.ntprproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.ntprproject.helper.Messages;
import tvz.ntpr.ntprproject.entity.Grade;
import tvz.ntpr.ntprproject.entity.Student;
import tvz.ntpr.ntprproject.entity.User;
import tvz.ntpr.ntprproject.security.AuthenticationService;
import tvz.ntpr.ntprproject.service.CourseService;
import tvz.ntpr.ntprproject.service.GradeService;
import tvz.ntpr.ntprproject.service.StudentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static tvz.ntpr.ntprproject.utils.DigitalSignature.sign;
import static tvz.ntpr.ntprproject.utils.HtmlToPdf.*;
import static tvz.ntpr.ntprproject.utils.ModelInitialization.initialize;
import static tvz.ntpr.ntprproject.utils.NotificationJNI.showNotification;
import static tvz.ntpr.ntprproject.utils.URLS.*;

@Controller
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

    @GetMapping(URL_STUDENT)
    public String showStudentView(Model model) {
        authenticationService.refresh();
        initModel(model);
        return "student";
    }

    @GetMapping(URL_STUDENT + "/download")
    public String saveStudentReport(Model model, RedirectAttributes redirectAttributes) {
        authenticationService.refresh();
        User user = (User) model.getAttribute("userLogin");

        String studentReportsDirectory = System.getProperty("user.home") + "\\Downloads\\ntpr.student_reports";
        String tmpFile = studentReportsDirectory + "\\student_report.tmp.pdf";
        try {
            if (!Files.exists(Paths.get(studentReportsDirectory)))
                Files.createDirectory(Paths.get(studentReportsDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }

        scrapeHtmlToPdfFile("http://127.0.0.1:8080/ntpr" + URL_STUDENT,
                user.getUserUuid(), tmpFile);
        sign(tmpFile, studentReportsDirectory + "\\student_report"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))
                + ".pdf");

        try {
            Files.deleteIfExists(Paths.get(tmpFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        showNotification(messages.getMessage("success.download"));

        redirectAttributes.addFlashAttribute("success", messages.getMessage("success.download"));
        return "redirect:" + URL_STUDENT;
    }

    @GetMapping(URL_STUDENT + "/{studentId}")
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