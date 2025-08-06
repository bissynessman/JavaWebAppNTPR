package tvz.ntpr.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.comparator.UserComparator;
import tvz.ntpr.core.helper.BooleanWrapper;
import tvz.ntpr.core.helper.DeleteBuffer;
import tvz.ntpr.core.enums.Role;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.helper.SpicedPassword;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static tvz.ntpr.core.utils.ModelInitialization.initialize;
import static tvz.ntpr.core.utils.PasswordUtils.hashPassword;
import static tvz.ntpr.core.utils.URLS.*;

@Controller
@AllArgsConstructor
@SessionAttributes("userLogin")
public class UsersController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final Messages messages;

    @GetMapping(URL_USERS)
    public String showUsersView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model);
        return "users";
    }

    @GetMapping(URL_USERS + "/edit/{userId}")
    public String editUser(Model model, @PathVariable String userId) {
        authenticationService.refresh();
        initEditModel(model, userId);
        return "editUser";
    }

    @PostMapping(URL_USERS + "/delete")
    public String deleteUser(Model model, RedirectAttributes redirectAttributes,
                             @ModelAttribute DeleteBuffer userBuffer) {
        authenticationService.refresh();
        List<String> usersToDelete = userBuffer.getItems().entrySet().stream()
                .filter(entry -> entry.getValue().getValue())
                .map(Map.Entry::getKey)
                .toList();

        if (!usersToDelete.isEmpty()) {
            userService.deleteUser(usersToDelete);
            redirectAttributes.addFlashAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            redirectAttributes.addFlashAttribute("error", messages.getMessage("error.missing-user"));

        initModel(model);
        return "redirect:" + URL_USERS;
    }

    @PostMapping(URL_USERS + "/edit/{userId}")
    public String processEditUser(Model model, RedirectAttributes redirectAttributes,
                                  @PathVariable String userId, @ModelAttribute User userUpdate) {
        authenticationService.refresh();
        User user = userService.getById(userId);

        if (user != null) {
            user.setUsername(userUpdate.getUsername());
            if (!userUpdate.getPassword().isEmpty()) {
                SpicedPassword spicedPassword = hashPassword(userUpdate.getPassword(), null, userUpdate.getUsername());
                user.setPassword(spicedPassword.getPasswordHash());
                user.setPasswordSalt(spicedPassword.getSalt());
            }
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            redirectAttributes.addFlashAttribute("error", messages.getMessage("error.no-user"));

        return "redirect:" + URL_USERS;
    }

    private void initEditModel(Model model, String userId) {
        initialize(model, URL_USERS + "/edit/" + userId);
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword("");
            model.addAttribute("userUpdate", user);
        }
    }

    private void initModel(Model model) {
        initialize(model, URL_USERS);
        List<User> usersList = userService.getAll().stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .sorted(new UserComparator())
                .toList();
        DeleteBuffer userBuffer = DeleteBuffer.builder()
                .items(userService.getAll().stream()
                        .collect(Collectors.toMap(User::getId, user -> new BooleanWrapper(false))))
                .build();
        model.addAttribute("usersList", usersList);
        model.addAttribute("userBuffer", userBuffer);
    }
}
