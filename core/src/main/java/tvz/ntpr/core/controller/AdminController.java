package tvz.ntpr.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.entity.User;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.Urls.*;

@Controller
@AllArgsConstructor
@SessionAttributes("userLogin")
public class AdminController {
    @GetMapping(URL_ADMIN)
    public String showAdminView(Model model) {
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "admin";
    }

    @PostMapping(URL_ADMIN)
    public String handleRedirect(Model model, RedirectAttributes redirectAttributes,
                                 @RequestParam("action") String action) {
        User userLogin = (User) model.getAttribute("userLogin");
        redirectAttributes.addFlashAttribute("userLogin", userLogin);

        return switch (action) {
            case "authorizeProf" -> "redirect:" + URL_AUTHORIZATION;
            case "editCourses" -> "redirect:" + URL_COURSES;
            case "editUsers" -> "redirect:" + URL_USERS;
            default -> {
                initModel(model);
                yield "admin";
            }
        };
    }

    private void initModel(Model model) {
        initialize(model, URL_ADMIN);
        model.addAttribute("admin", model.getAttribute("userLogin"));
    }
}
