package tvz.ntpr.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.comparator.ProfessorComparator;
import tvz.ntpr.core.helper.ProfessorBuffer;
import tvz.ntpr.core.entity.Professor;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.ProfessorService;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.URLS.*;

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
