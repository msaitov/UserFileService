package ru.msaitov.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msaitov.model.Role;
import ru.msaitov.service.userAccess.UserAccess;
import ru.msaitov.view.UserView;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Контроллер Права доступа
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final UserAccess userAccess;

    private static Logger logger;

    @Autowired
    public RoleController(UserAccess userAccess, Logger logger) {
        this.userAccess = userAccess;
        RoleController.logger = logger;
    }

    /**
     * Получить список всех пользователей
     *
     * @param model - передача параметров в фронт
     * @return переход на страницу userRole
     */
    @GetMapping("/userRole")
    public String getUserRole(Model model) {
        logger.info("[CONTROLLER] getUserRole");
        model.addAttribute("users", userAccess.getAllUser());
        return "userRole";
    }

    /**
     * Получить Id пользователя
     *
     * @param userId - id пользователя
     * @param model - передача параметров в фронт
     * @return переход на страницу userEdit
     */
    @GetMapping("/userRole/{userId}")
    public String getUserRole(@PathVariable Long userId, Model model) {
        logger.info("[CONTROLLER] getUserRole with id");
        UserView userView = userAccess.getUserViewById(userId);
        model.addAttribute("user", userView);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    /**
     * Обработка кнопки Сохранить измененные Права доступа
     *
     * @param email - email пользователя
     * @param form - Role
     * @return переход на страницу userRole
     */
    @PostMapping("/userRole")
    public String postUserRole(
            @RequestParam String email,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") Long userId) {

        logger.info("[CONTROLLER] postUserRole");
        UserView userView = userAccess.getUserViewById(userId);
        userView.setEmail(email);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        userView.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                userView.getRoles().add(Role.valueOf(key));
            }
        }

        userAccess.saveUser(userView);

        return "redirect:/userRole";
    }


}
