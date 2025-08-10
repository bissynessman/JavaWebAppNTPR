package tvz.ntpr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Course;
import tvz.ntpr.api.entity.Grade;
import tvz.ntpr.api.service.CourseService;
import tvz.ntpr.api.service.GradeService;

import java.util.List;

import static tvz.ntpr.api.config.Urls.*;

@RestController
@RequestMapping(URL_COURSE)
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

    @GetMapping(URL_ID)
    public Course getCourseById(@PathVariable String id) {
        return courseService.getById(id);
    }

    @GetMapping(URL_PROFESSOR_ID)
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

    @DeleteMapping(URL_ID)
    public void deleteCourse(@PathVariable String id) {
        for (Grade grade : gradeService.getByCourseId(id))
            gradeService.deleteById(grade.getId());
        courseService.deleteById(id);
    }
}
