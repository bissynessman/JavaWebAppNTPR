package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.helper.Mail;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.UserService;
import tvz.ntpr.core.utils.AttachmentUtils;
import tvz.ntpr.core.utils.EmailService;

import java.util.List;

import static tvz.ntpr.core.config.Urls.URL_MAIL;
import static tvz.ntpr.core.utils.ModelInitialization.initialize;

@Controller
@RequestMapping(URL_MAIL)
@SessionAttributes({ "userLogin" })
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public EmailController(EmailService emailService,
                           UserService userService,
                           AuthenticationService authenticationService,
                           Messages messages) {
        this.emailService = emailService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String view(Model model) {
        initModel(model);
        return "mail";
    }

    @PostMapping
    public String sendEmail(@RequestParam String from,
                            @RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String body,
                            @RequestParam MultipartFile[] attachments,
                            Model model) {
        Mail mail = new Mail(from, to, subject, body, AttachmentUtils.fromMultipartFiles(attachments));
        try {
            emailService.sendEmail(mail);
            model.addAttribute("success", messages.getMessage("success.email"));
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", messages.getMessage("error.email"));
        }
        initModel(model);
        return "mail";
    }

    private void initModel(Model model) {
        initialize(model, URL_MAIL);
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        List<User> usersList = userService.getAll().stream()
                .filter(user -> !user.getId().equals(userLogin.getId()))
                .toList();
        model.addAttribute("from", userLogin.getUsername());
        model.addAttribute("usersList", usersList);
    }
}
