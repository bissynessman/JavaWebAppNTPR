package tvz.ntpr.ntprdbrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.ntprdbrestapi.entity.Course;
import tvz.ntpr.ntprdbrestapi.entity.Professor;
import tvz.ntpr.ntprdbrestapi.service.CourseService;
import tvz.ntpr.ntprdbrestapi.service.ProfessorService;

import java.util.List;

@RestController
@RequestMapping("/professors")
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private CourseService courseService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<Professor> getAll() {
        return professorService.getAll();
    }

    @GetMapping("/{id}")
    public Professor getById(@PathVariable String id) {
        return professorService.getById(id);
    }

    @GetMapping("/unauthorized")
    public List<Professor> getUnauthorized() {
        return professorService.getAllUnauthorized();
    }

    @PostMapping
    public void saveProfessor(@RequestBody Professor professor) {
        professorService.create(professor);
    }

    @PutMapping
    public void updateProfessor(@RequestBody Professor professor) {
        professorService.update(professor);
    }

    @PutMapping("/authorize")
    public void authorize(@RequestBody Professor professor) {
        professorService.authorize(professor.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable String id) {
        for (Course course : courseService.getByProfessorId(id)) {
            course.setProfessor(null);
            courseService.update(course);
        }
        professorService.deleteById(id);
    }
}
