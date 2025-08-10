package tvz.ntpr.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Grade;
import tvz.ntpr.api.entity.Student;
import tvz.ntpr.api.service.GradeService;
import tvz.ntpr.api.service.StudentService;

import java.util.List;

import static tvz.ntpr.api.config.Urls.URL_ID;
import static tvz.ntpr.api.config.Urls.URL_STUDENT;

@RestController
@RequestMapping(URL_STUDENT)
public class StudentController {
    private final StudentService studentService;
    private final GradeService gradeService;
    
    public StudentController(StudentService studentService, GradeService gradeService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
    }
    
    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }
    
    @GetMapping(URL_ID)
    public Student getById(@PathVariable String id) {
        return studentService.getById(id);
    }

    @PostMapping
    public void saveStudent(@RequestBody Student student) {
        studentService.create(student);
    }

    @PutMapping
    public void updateStudent(@RequestBody Student student) {
        studentService.update(student);
    }

    @DeleteMapping(URL_ID)
    public void deleteGrade(@PathVariable String id) {
        for (Grade grade : gradeService.getByStudentId(id))
            gradeService.deleteById(grade.getId());
        studentService.deleteById(id);
    }
}
