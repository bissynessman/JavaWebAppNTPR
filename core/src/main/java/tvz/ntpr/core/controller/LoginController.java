package tvz.ntpr.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.JwtHolder;
import tvz.ntpr.core.helper.LoginRequest;
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
import static tvz.ntpr.core.config.Urls.*;

@Controller
@RequestMapping(URL_LOGIN)
public class LoginController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final Messages messages;
    private final JwtHolder jwtHolder;

    public LoginController(UserService userService,
                           AuthenticationService authenticationService,
                           Messages messages,
                           JwtHolder jwtHolder) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.messages = messages;
        this.jwtHolder = jwtHolder;
    }

    @GetMapping
    public String showLoginView(Model model) {
        initModel(model);
        return "login";
    }

    @PostMapping
    public String processLogin(Model model, RedirectAttributes redirectAttributes, LoginRequest userLogin) {
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
        model.addAttribute("userLogin", new LoginRequest());
    }
}
