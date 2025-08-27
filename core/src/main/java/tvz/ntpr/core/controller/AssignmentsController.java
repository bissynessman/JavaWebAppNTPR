package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.core.entity.Assignment;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.AssignmentService;
import tvz.ntpr.core.service.CourseService;

import static tvz.ntpr.core.config.Urls.*;
import static tvz.ntpr.core.utils.ModelInitialization.initialize;

@Controller
@RequestMapping(URL_ASSIGNMENT)
@SessionAttributes("userLogin")
public class AssignmentsController {
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public AssignmentsController(AssignmentService assignmentService,
                                 CourseService courseService,
                                 AuthenticationService authenticationService,
                                 Messages messages) {
        this.assignmentService = assignmentService;
        this.courseService = courseService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping(URL_ASSIGNMENT_ID)
    public String assignment(Model model, @PathVariable String assignmentId) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model, assignmentId);
        return "assignments";
    }

    @PostMapping(URL_ASSIGNMENT_ID)
    public String updateAssignment(Model model, Assignment assignment) {
        authenticationService.refresh();
        assignmentService.updateAssignment(assignment);
        return "redirect:" + URL_STUDENT;
    }

    private void initModel(Model model, String assignmentId) {
        initialize(model, URL_ASSIGNMENT + "/" + assignmentId);
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        String courseName = courseService.getCourseById(assignment.getCourse()).getName();
        model.addAttribute("assignment", assignment);
        model.addAttribute("courseName", courseName);
    }
}
