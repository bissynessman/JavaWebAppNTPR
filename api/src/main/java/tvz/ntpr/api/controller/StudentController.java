package tvz.ntpr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Grade;
import tvz.ntpr.api.entity.Student;
import tvz.ntpr.api.service.GradeService;
import tvz.ntpr.api.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private GradeService gradeService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }
    
    @GetMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable String id) {
        for (Grade grade : gradeService.getByStudentId(id))
            gradeService.deleteById(grade.getId());
        studentService.deleteById(id);
    }
}
