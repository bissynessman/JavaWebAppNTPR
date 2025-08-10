package tvz.ntpr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Grade;
import tvz.ntpr.api.service.GradeService;

import java.util.List;

import static tvz.ntpr.api.config.Urls.*;

@RestController
@RequestMapping(URL_GRADE)
public class GradeController {
    @Autowired
    private GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<Grade> getAll() {
        return gradeService.getAll();
    }

    @GetMapping(URL_ID)
    public Grade getById(@PathVariable String id) {
        return gradeService.getById(id);
    }

    @GetMapping(URL_STUDENT_ID)
    public List<Grade> getGradesByStudent(@PathVariable String studentId) {
        return gradeService.getByStudentId(studentId);
    }

    @GetMapping(URL_COURSE_ID)
    public List<Grade> getGradesByCourse(@PathVariable String courseId) {
        return gradeService.getByCourseId(courseId);
    }

    @PostMapping
    public void saveGrade(@RequestBody Grade grade) {
        gradeService.create(grade);
    }

    @PutMapping
    public void updateGrade(@RequestBody Grade grade) {
        gradeService.update(grade);
    }

    @DeleteMapping(URL_ID)
    public void deleteGrade(@PathVariable String id) {
        gradeService.deleteById(id);
    }
}
