package tvz.ntpr.ntprproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tvz.ntpr.ntprproject.helper.Messages;
import tvz.ntpr.ntprproject.comparator.ProfessorComparator;
import tvz.ntpr.ntprproject.helper.ProfessorBuffer;
import tvz.ntpr.ntprproject.entity.Professor;
import tvz.ntpr.ntprproject.entity.User;
import tvz.ntpr.ntprproject.security.AuthenticationService;
import tvz.ntpr.ntprproject.service.ProfessorService;

import static tvz.ntpr.ntprproject.utils.ModelInitialization.initialize;
import static tvz.ntpr.ntprproject.utils.URLS.*;

@Controller
@AllArgsConstructor
public class AuthorizationController {
    @Autowired
    private final ProfessorService professorService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;

    @GetMapping(URL_AUTHORIZATION)
    public String showAuthorizationView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "authorization";
    }

    @PostMapping(URL_AUTHORIZATION)
    public String processAuthorization(Model model, @ModelAttribute ProfessorBuffer professorBuffer) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        if (!professorBuffer.getProfessors().isEmpty()) {
            for (Professor professor : professorBuffer.getProfessors())
                professorService.authorizeProfessor(professor.getId());
            model.addAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            model.addAttribute("error", messages.getMessage("error.missing-professor"));

        initModel(model);
        return "authorization";
    }

    private void initModel(Model model) {
        initialize(model, URL_AUTHORIZATION);
        ProfessorBuffer professorBuffer = ProfessorBuffer.builder()
                .professors(professorService.getUnauthorizedProfessors().stream()
                        .sorted(new ProfessorComparator())
                        .toList())
                .build();
        model.addAttribute("professorBuffer", professorBuffer);
    }
}
