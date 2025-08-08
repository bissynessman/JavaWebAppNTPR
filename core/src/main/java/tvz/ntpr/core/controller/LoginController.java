package tvz.ntpr.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.JwtHolder;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.enums.Role;
import tvz.ntpr.core.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.UserService;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.Urls.*;

@Controller
@AllArgsConstructor
public class LoginController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;
    @Autowired
    private final JwtHolder jwtHolder;

    @GetMapping(URL_LOGIN)
    public String showLoginView(Model model) {
        initModel(model);
        return "login";
    }

    @PostMapping(URL_LOGIN)
    public String processLogin(Model model, RedirectAttributes redirectAttributes, User userLogin) {
        try {
            authenticationService.login(userLogin.getUsername(), userLogin.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jwtHolder.getAccessToken() == null)
            model.addAttribute("error", messages.getMessage("error.invalid-username-password"));
        else {
            User user = userService.getByUsername(userLogin.getUsername());
            redirectAttributes.addFlashAttribute("userLogin", user);
            if (user.getRole().equals(Role.ADMIN))
                return "redirect:" + URL_ADMIN;
            else if (user.getRole().equals(Role.PROFESSOR))
                return "redirect:" + URL_PROFESSOR;
            else if (user.getRole().equals(Role.STUDENT))
                return "redirect:" + URL_STUDENT;
        }

        initModel(model);
        return "login";
    }

    void initModel(Model model) {
        initialize(model, URL_LOGIN);
        model.addAttribute("userLogin", User.builder().build());
    }
}
