package ru.msaitov.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msaitov.controller.exception.UserAlreadyExistException;
import ru.msaitov.service.verificationToken.TokenState;
import ru.msaitov.service.verificationToken.VerificationTokenService;
import ru.msaitov.service.verificationUser.UserService;
import ru.msaitov.view.UserView;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер регистрация и авторизация пользователя
 */
@Controller
public class UserController {

    private final UserService userService;

    private final VerificationTokenService tokenService;

    private static Logger logger;

    @Autowired
    public UserController(UserService userService, VerificationTokenService tokenService, Logger logger) {
        this.userService = userService;
        this.tokenService = tokenService;
        UserController.logger = logger;
    }

    /**
     * Перенаправление на страницу console
     *
     * @return переход на страницу console
     */
    @GetMapping("/")
    public String redirecLogin() {
        logger.info("[CONTROLLER] redirecLogin");
        return "redirect:/console";
    }

    /**
     * Регистрация
     *
     * @return переход на страницу registration
     */
    @GetMapping("/registration")
    public String getRegistration() {
        logger.info("[CONTROLLER] getRegistration");
        return "registration";
    }

    /**
     * Обработка формы регистрации
     *
     * @param userView  - получение текущего пользователя
     * @param request  - предоставления информации запроса для сервлетов HTTP
     * @return переход на страницу registrationConfirmBegin
     */
    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute UserView userView, final HttpServletRequest request) {
        logger.info("[CONTROLLER] postRegistration");
        if (userView.getEmail().isEmpty() || userView.getPassword().isEmpty()) {
            return "registration";
        }
        try {
            userService.registerNewUserAccount(userView, getAppUrl(request));
        } catch (UserAlreadyExistException e) {
            return "userAlreadyExist";
        }
        return "registrationConfirm/registrationConfirmBegin";
    }

    /**
     * Обработка ссылки для активации пользователя
     *
     * @param token - Токен пользователя
     * @return переход на страницу registrationConfirmValid
     */
    @GetMapping(value = "/registrationConfirm*")
    public String confirmRegistration(@RequestParam("token") final String token) {
        logger.info("[CONTROLLER] confirmRegistration");
        final TokenState tokenState = tokenService.validateVerificationToken(token);
        switch (tokenState) {
            case TOKEN_INVALID:
                return "registrationConfirm/registrationConfirmInvalid";
            case TOKEN_EXPIRED:
                return "registrationConfirm/registrationConfirmExpired";
        }
        return "registrationConfirm/registrationConfirmValid";
    }

    private String getAppUrl(HttpServletRequest request) {
        logger.info("[CONTROLLER] getAppUrl");
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}