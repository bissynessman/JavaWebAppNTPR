package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.comparator.CourseComparator;
import tvz.ntpr.core.helper.BooleanWrapper;
import tvz.ntpr.core.helper.DeleteBuffer;
import tvz.ntpr.core.helper.CourseWrapper;
import tvz.ntpr.core.entity.Course;
import tvz.ntpr.core.entity.Professor;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.CourseService;
import tvz.ntpr.core.service.ProfessorService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.config.Urls.*;

@Controller
@RequestMapping(URL_COURSES)
@SessionAttributes("userLogin")
public class CoursesController {
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public CoursesController(CourseService courseService,
                             ProfessorService professorService,
                             AuthenticationService authenticationService,
                             Messages messages) {
        this.courseService = courseService;
        this.professorService = professorService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String showCoursesView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "courses";
    }

    @PostMapping(URL_DELETE)
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

    @PostMapping
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
