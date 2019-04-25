package ru.msaitov.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static Logger logger = LogManager.getFormatterLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, VerificationTokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    /**
     * Перенаправление на страницу console
     *
     * @return переход на страницу console
     */
    @GetMapping("/")
    public String redirecLogin() {
        logger.info("Перенаправление на страницу console");
        return "redirect:/console";
    }

    /**
     * Регистрация
     *
     * @return переход на страницу registration
     */
    @GetMapping("/registration")
    public String getRegistration() {
        logger.info("Страница registration");
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
        logger.info("Обработка формы регистрации для пользователя: {}", userView.getEmail());
        if (userView.getEmail().isEmpty() || userView.getPassword().isEmpty()) {
            return "registration";
        }
        try {
            userService.registerNewUserAccount(userView, getAppUrl(request));
        } catch (UserAlreadyExistException e) {
            logger.info("Пользователь: {}, уже существует", userView.getEmail());
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
        logger.info("Обработка ссылки для активации пользователя.");
        final TokenState tokenState = tokenService.validateVerificationToken(token);
        switch (tokenState) {
            case TOKEN_INVALID:
                logger.info("Токен: {}, недействителен", token);
                return "registrationConfirm/registrationConfirmInvalid";
            case TOKEN_EXPIRED:
                logger.info("Токен: {}, срок действия истек", token);
                return "registrationConfirm/registrationConfirmExpired";
        }
        logger.info("Токен: {}, регистрация подтверждена", token);
        return "registrationConfirm/registrationConfirmValid";
    }

    private String getAppUrl(HttpServletRequest request) {
        logger.info("Формирование ссылки для активации токена");
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}