package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.enums.Role;
import tvz.ntpr.core.helper.SpicedPassword;
import tvz.ntpr.core.helper.UserToRegister;
import tvz.ntpr.core.entity.Professor;
import tvz.ntpr.core.entity.Student;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.ProfessorService;
import tvz.ntpr.core.service.StudentService;
import tvz.ntpr.core.service.UserService;

import java.util.regex.*;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.PasswordUtils.hashPassword;
import static tvz.ntpr.core.config.Urls.*;

@Controller
@RequestMapping(URL_PROFILE)
@SessionAttributes({"userToRegister", "profile"})
public class ProfileController {
    private final UserService userService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public ProfileController(UserService userService,
                             ProfessorService professorService,
                             StudentService studentService,
                             AuthenticationService authenticationService,
                             Messages messages) {
        this.userService = userService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String showProfileView(Model model) {
        authenticationService.refresh();
        initModel(model);
        return "profile";
    }

    @PostMapping
    public String handleProfileSetup(Model model, RedirectAttributes redirectAttributes,
                                     UserToRegister userToRegister,
                                     @ModelAttribute("profile") Object profile) {
        if (profile instanceof Professor professorProfile) {
            Professor newProfessor = Professor.builder()
                    .firstName(professorProfile.getFirstName())
                    .lastName(professorProfile.getLastName())
                    .build();
            if (!alphaCheck(newProfessor.getFirstName())
                    || !alphaCheck(newProfessor.getLastName())) {
                model.addAttribute("error", messages.getMessage("error.invalid-characters"));
                return "profile";
            }
            professorService.saveProfessor(newProfessor);
            User savedUser = registerUser(userToRegister, newProfessor.getId());
            redirectAttributes.addFlashAttribute("userLogin", savedUser);
            return "redirect:" + URL_PROFESSOR;
        } else if (profile instanceof Student studentProfile) {
            Student newStudent = Student.builder()
                    .jmbag(studentProfile.getJmbag())
                    .firstName(studentProfile.getFirstName())
                    .lastName(studentProfile.getLastName())
                    .major(studentProfile.getMajor())
                    .build();
            if (!alphaCheck(newStudent.getFirstName())
                    || !alphaCheck(newStudent.getLastName())) {
                model.addAttribute("error", messages.getMessage("error.invalid-characters"));
                return "profile";
            } else if (!jmbagCheck(newStudent.getJmbag())) {
                model.addAttribute("error", messages.getMessage("error.invalid-jmbag"));
                return "profile";
            }
            studentService.saveStudent(newStudent);
            User savedUser = registerUser(userToRegister, newStudent.getId());
            redirectAttributes.addFlashAttribute("userLogin", savedUser);
            return "redirect:" + URL_STUDENT;
        }

        return "profile";
    }

    private User registerUser(UserToRegister userToRegister, String userUuid) {
        SpicedPassword spicedPassword = hashPassword(userToRegister.getPassword(), null, userToRegister.getUsername());
        User newUser = User.builder()
                .username(userToRegister.getUsername())
                .password(spicedPassword.getPasswordHash())
                .passwordSalt(spicedPassword.getSalt())
                .role(userToRegister.getRole())
                .userUuid(userUuid)
                .build();
        userService.saveUser(newUser);
        return newUser;
    }

    private boolean alphaCheck(String string) {
        String regex = "([a-z]|[A-Z])+";
        Pattern pattern = Pattern.compile(regex);

        if (string == null) return false;
        else return pattern.matcher(string).matches();
    }

    private boolean jmbagCheck(String string) {
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);

        if (string == null) return false;
        else return pattern.matcher(string).matches();
    }

    private void initModel(Model model) {
        initialize(model, URL_PROFILE);
        UserToRegister userToRegister = (UserToRegister) model.getAttribute("userToRegister");
        if (userToRegister.getRole().equals(Role.PROFESSOR))
            model.addAttribute("profile", Professor.builder().build());
        if (userToRegister.getRole().equals(Role.STUDENT))
            model.addAttribute("profile", Student.builder().build());
    }
}
