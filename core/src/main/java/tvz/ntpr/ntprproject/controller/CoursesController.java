package tvz.ntpr.ntprproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.ntprproject.helper.Messages;
import tvz.ntpr.ntprproject.comparator.CourseComparator;
import tvz.ntpr.ntprproject.helper.BooleanWrapper;
import tvz.ntpr.ntprproject.helper.DeleteBuffer;
import tvz.ntpr.ntprproject.helper.CourseWrapper;
import tvz.ntpr.ntprproject.entity.Course;
import tvz.ntpr.ntprproject.entity.Professor;
import tvz.ntpr.ntprproject.entity.User;
import tvz.ntpr.ntprproject.security.AuthenticationService;
import tvz.ntpr.ntprproject.service.CourseService;
import tvz.ntpr.ntprproject.service.ProfessorService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tvz.ntpr.ntprproject.utils.ModelInitialization.initialize;
import static tvz.ntpr.ntprproject.utils.URLS.*;

@Controller
@AllArgsConstructor
@SessionAttributes("userLogin")
public class CoursesController {
    @Autowired
    private final CourseService courseService;
    @Autowired
    private final ProfessorService professorService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;

    @GetMapping(URL_COURSES)
    public String showCoursesView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "courses";
    }

    @PostMapping(URL_COURSES + "/delete")
    public String processUpdates(Model model, RedirectAttributes redirectAttributes, @ModelAttribute DeleteBuffer courseBuffer) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        List<String> coursesToDelete = courseBuffer.getItems().entrySet().stream()
                .filter(entry -> entry.getValue().getValue())
                .map(Map.Entry::getKey)
                .toList();

        if (!coursesToDelete.isEmpty()) {
            courseService.deleteCourses(coursesToDelete);
            redirectAttributes.addFlashAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            redirectAttributes.addFlashAttribute("error", messages.getMessage("error.missing-course"));

        return "redirect:" + URL_COURSES;
    }

    @PostMapping(URL_COURSES)
    public String processAddCourse(Model model, @ModelAttribute CourseWrapper newCourse) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        newCourse.getCourse().setProfessor(professorService.getProfessorById(
                newCourse.getProfessorId()).getId());
        courseService.saveCourse(newCourse.getCourse());

        initModel(model);
        return "courses";
    }

    private void initModel(Model model) {
        initialize(model, URL_COURSES);
        List<Course> coursesList = courseService.getAll().stream()
                .sorted(new CourseComparator())
                .toList();
        DeleteBuffer courseBuffer = DeleteBuffer.builder()
                .items(courseService.getAll().stream()
                        .collect(Collectors.toMap(Course::getId, course -> new BooleanWrapper(false))))
                .build();
        List<Professor> professorList = professorService.getAll();
        model.addAttribute("coursesList", coursesList);
        model.addAttribute("courseBuffer", courseBuffer);
        model.addAttribute("newCourse", new CourseWrapper());
        model.addAttribute("professorList", professorList);
    }
}
