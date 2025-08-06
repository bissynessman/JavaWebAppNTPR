package tvz.ntpr.ntprproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tvz.ntpr.ntprproject.helper.Messages;
import tvz.ntpr.ntprproject.comparator.CourseComparator;
import tvz.ntpr.ntprproject.comparator.StudentComparator;
import tvz.ntpr.ntprproject.entity.*;
import tvz.ntpr.ntprproject.security.AuthenticationService;
import tvz.ntpr.ntprproject.service.CourseService;
import tvz.ntpr.ntprproject.service.GradeService;
import tvz.ntpr.ntprproject.service.ProfessorService;
import tvz.ntpr.ntprproject.service.StudentService;

import java.util.List;

import static tvz.ntpr.ntprproject.utils.ModelInitialization.initialize;
import static tvz.ntpr.ntprproject.utils.URLS.*;

@Controller
@AllArgsConstructor
@SessionAttributes({"userLogin", "grade"})
public class GradesController {
    @Autowired
    private final GradeService gradeService;
    @Autowired
    private final StudentService studentService;
    @Autowired
    private final CourseService courseService;
    @Autowired
    private final ProfessorService professorService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;

    @GetMapping(URL_GRADES)
    public String showGradesView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        Professor professor = professorService.getProfessorById(userLogin.getUserUuid());
        initModel(model, userLogin, professor);
        return "grades";
    }

    @PostMapping(URL_GRADES)
    private String processGradeInput(Model model, Grade grade) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        Professor professor = professorService.getProfessorById(userLogin.getUserUuid());
        if (professor.isAuthorized()) {
            Grade newGrade = Grade.builder()
                    .student(grade.getStudent())
                    .course(grade.getCourse())
                    .grade(grade.getGrade())
                    .build();

            gradeService.saveGrade(newGrade);
            model.addAttribute("success", messages.getMessage("success.grade-input"));
        } else
            model.addAttribute("error", messages.getMessage("error.bad-authorization"));

        initModel(model, userLogin, professor);
        return "grades";
    }

    private void initModel(Model model, User userLogin, Professor professor) {
        initialize(model, URL_GRADES);
        List<Student> studentList = studentService.getAll().stream()
                .sorted(new StudentComparator())
                .toList();
        List<Course> courseList = courseService.getCoursesByProfessor(professor.getId()).stream()
                .sorted(new CourseComparator())
                .toList();
        model.addAttribute("userLogin", userLogin);
        model.addAttribute("studentList", studentList);
        model.addAttribute("courseList", courseList);
        model.addAttribute("grade", Grade.builder().build());
    }
}
