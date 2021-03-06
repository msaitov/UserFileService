package ru.msaitov.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static Logger logger = LogManager.getFormatterLogger(RoleController.class);

    @Autowired
    public RoleController(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Получить список всех пользователей c правами доступа
     *
     * @param model - передача параметров в фронт
     * @return переход на страницу userRole
     */
    @GetMapping("/userRole")
    public String getUserRole(Model model) {
        logger.info("Получить список всех пользователей c правами доступа");
        model.addAttribute("users", userAccess.getAllUser());
        return "userRole";
    }

    /**
     * Редактировать роль пользователя
     *
     * @param userId - id пользователя
     * @param model - передача параметров в фронт
     * @return переход на страницу userEdit
     */
    @GetMapping("/userRole/{userId}")
    public String getUserRole(@PathVariable Long userId, Model model) {
        UserView userView = userAccess.getUserViewById(userId);
        logger.info("Редактировать роль пользователя: {}", userView.getEmail());
        model.addAttribute("user", userView);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    /**
     * Обработка кнопки Сохранить измененные роль пользователя
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

        UserView userView = userAccess.getUserViewById(userId);
        userView.setEmail(email);
        logger.info("Обработка кнопки Сохранить измененные роль пользователя {}", email);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        userView.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                userView.getRoles().add(Role.valueOf(key));
                logger.info("Новая роль: {}, пользователя: {}", Role.valueOf(key), email);
            }
        }

        userAccess.saveUser(userView);

        return "redirect:/userRole";
    }


}
