package tvz.ntpr.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Assignment;
import tvz.ntpr.api.service.AssignmentService;

import java.util.List;

import static tvz.ntpr.api.config.Urls.*;

@RestController
@RequestMapping(URL_ASSIGNMENT)
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAll();
    }

    @GetMapping(URL_ID)
    public Assignment getAssignmentById(@PathVariable String id) {
        return assignmentService.getById(id);
    }

    @GetMapping(STUDENT_FILTER)
    public List<Assignment> getActiveForStudent(@PathVariable String studentId) {
        return assignmentService.getActiveForStudent(studentId);
    }

    @GetMapping(COURSE_FILTER)
    public List<Assignment> getAllForCourse(@PathVariable String courseId) {
        return assignmentService.getAllForCourse(courseId);
    }

    @GetMapping(ASSIGNMENT_FILTER)
    public List<Assignment> getAllForAssignment(@PathVariable String assignmentId) {
        return assignmentService.getAllForAssigment(assignmentId);
    }

    @PostMapping
    public void saveGrade(@RequestBody Assignment assignment) {
        assignmentService.create(assignment);
    }

    @PutMapping
    public void updateGrade(@RequestBody Assignment assignment) {
        assignmentService.update(assignment);
    }

    @DeleteMapping(URL_ID)
    public void deleteCourse(@PathVariable String id) {
        assignmentService.deleteById(id);
    }
}
