package tvz.ntpr.ntprdbrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.ntprdbrestapi.entity.Course;
import tvz.ntpr.ntprdbrestapi.entity.Grade;
import tvz.ntpr.ntprdbrestapi.entity.User;
import tvz.ntpr.ntprdbrestapi.enums.Role;
import tvz.ntpr.ntprdbrestapi.service.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private GradeService gradeService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        return userService.getById(id);
    }

    @GetMapping("/username/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PostMapping
    public void saveUser(@RequestBody User user) {
        userService.create(user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable String id) {
        User user = userService.getById(id);
        if (user.getRole().equals(Role.STUDENT)) {
            for (Grade grade : gradeService.getByStudentId(user.getUserUuid()))
                gradeService.deleteById(grade.getId());
            studentService.deleteById(user.getUserUuid());
        } else if (user.getRole().equals(Role.PROFESSOR)) {
            for (Course course : courseService.getByProfessorId(user.getUserUuid())) {
                for (Grade grade : gradeService.getByCourseId(course.getId()))
                    gradeService.deleteById(grade.getId());
                courseService.deleteById(course.getId());
            }
            professorService.deleteById(user.getUserUuid());
        }
        userService.deleteById(id);
    }
}
