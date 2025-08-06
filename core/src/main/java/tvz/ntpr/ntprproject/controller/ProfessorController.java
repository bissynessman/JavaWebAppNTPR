package tvz.ntpr.ntprproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.ntprproject.entity.Professor;
import tvz.ntpr.ntprproject.entity.User;
import tvz.ntpr.ntprproject.security.AuthenticationService;
import tvz.ntpr.ntprproject.service.ProfessorService;

import static tvz.ntpr.ntprproject.utils.ModelInitialization.initialize;
import static tvz.ntpr.ntprproject.utils.URLS.*;

@Controller
@AllArgsConstructor
@SessionAttributes({"userLogin", "action"})
public class ProfessorController {
    @Autowired
    private final ProfessorService professorService;
    @Autowired
    private final AuthenticationService authenticationService;

    @GetMapping(URL_PROFESSOR)
    public String showProfessorView(Model model) {
        authenticationService.refresh();
        initModel(model);
        return "professor";
    }

    @PostMapping(URL_PROFESSOR)
    public String handleRedirects(Model model, RedirectAttributes redirectAttributes,
                                  @RequestParam("action") String action) {
        User userLogin = (User) model.getAttribute("userLogin");
        redirectAttributes.addFlashAttribute("userLogin", userLogin);
        return switch (action) {
            case "addGrade" -> "redirect:" + URL_GRADES;
            case "viewStudents" -> "redirect:" + URL_VIEW_STUDENTS;
            default -> {
                initModel(model);
                yield "professor";
            }
        };
    }

    private void initModel(Model model) {
        initialize(model, URL_PROFESSOR);
        User userLogin = (User) model.getAttribute("userLogin");
        Professor professor = professorService.getProfessorById(userLogin.getUserUuid());
        model.addAttribute("professor", professor);
    }
}