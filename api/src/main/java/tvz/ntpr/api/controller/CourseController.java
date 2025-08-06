package tvz.ntpr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Course;
import tvz.ntpr.api.entity.Grade;
import tvz.ntpr.api.service.CourseService;
import tvz.ntpr.api.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private GradeService gradeService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable String id) {
        return courseService.getById(id);
    }

    @GetMapping("/professor/{professorId}")
    public List<Course> getGradesByStudent(@PathVariable String professorId) {
        return courseService.getByProfessorId(professorId);
    }

    @PostMapping
    public void saveGrade(@RequestBody Course course) {
        courseService.create(course);
    }

    @PutMapping
    public void updateGrade(@RequestBody Course course) {
        courseService.update(course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable String id) {
        for (Grade grade : gradeService.getByCourseId(id))
            gradeService.deleteById(grade.getId());
        courseService.deleteById(id);
    }
}
