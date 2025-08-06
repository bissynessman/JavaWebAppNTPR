package tvz.ntpr.ntprdbrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.ntprdbrestapi.entity.Grade;
import tvz.ntpr.ntprdbrestapi.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/grades")
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

    @GetMapping("/{id}")
    public Grade getById(@PathVariable String id) {
        return gradeService.getById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Grade> getGradesByStudent(@PathVariable String studentId) {
        return gradeService.getByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}")
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

    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable String id) {
        gradeService.deleteById(id);
    }
}
