package tvz.ntpr.ntprproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.ntprproject.comparator.StudentComparator;
import tvz.ntpr.ntprproject.helper.MajorWrapper;
import tvz.ntpr.ntprproject.entity.Student;
import tvz.ntpr.ntprproject.entity.User;
import tvz.ntpr.ntprproject.security.AuthenticationService;
import tvz.ntpr.ntprproject.service.StudentService;

import java.util.List;

import static tvz.ntpr.ntprproject.utils.ModelInitialization.initialize;
import static tvz.ntpr.ntprproject.utils.URLS.*;

@Controller
@AllArgsConstructor
@SessionAttributes({"userLogin", "majorFilter"})
public class ViewStudentsController {
    @Autowired
    private final StudentService studentService;
    @Autowired
    private final AuthenticationService authenticationService;

    @GetMapping(URL_VIEW_STUDENTS)
    public String showStudentsView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        initModel(model, false);
        return "students";
    }

    @PostMapping(URL_VIEW_STUDENTS)
    public String handleSort(Model model, @ModelAttribute MajorWrapper majorFilter) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        if (majorFilter.getMajor() != null) {
            List<Student> studentList = studentService.getAll().stream()
                    .filter(student -> student.getMajor().equals(majorFilter.getMajor()))
                    .toList();
            model.addAttribute("studentList", studentList);
            initModel(model, true);
        } else initModel(model, false);
        return "students";
    }

    private void initModel(Model model, Boolean filter) {
        initialize(model, URL_VIEW_STUDENTS);
        if (!filter) {
            List<Student> studentList = studentService.getAll().stream()
                    .sorted(new StudentComparator())
                    .toList();
            model.addAttribute("studentList", studentList);
        }
        model.addAttribute("majorFilter", new MajorWrapper());
    }
}
