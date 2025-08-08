package tvz.ntpr.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.helper.UserToRegister;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.UserService;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.Urls.*;

@Controller
@AllArgsConstructor
public class SignupController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;

    @GetMapping(URL_SIGNUP)
    public String showRegisterView(Model model) {
        initModel(model);
        return "signup";
    }

    @PostMapping(URL_SIGNUP)
    public String processSignup(Model model, RedirectAttributes redirectAttributes,
                                  UserToRegister userToRegister) {
        authenticationService.signup(userToRegister);
        if (userService.getByUsername(userToRegister.getUsername()) != null) {
            model.addAttribute("error", messages.getMessage("error.username-exists"));
            return "signup";
        } else if (!userToRegister.getPassword().equals(userToRegister.getRepeatPassword())) {
            model.addAttribute("error", messages.getMessage("error.password-match"));
            return "signup";
        }

        redirectAttributes.addFlashAttribute("userToRegister", userToRegister);
        return "redirect:" + URL_PROFILE;
    }

    private void initModel(Model model) {
        initialize(model, URL_SIGNUP);
        model.addAttribute("userToRegister", new UserToRegister());
    }
}
