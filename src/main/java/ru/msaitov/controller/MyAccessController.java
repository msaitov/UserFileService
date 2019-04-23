package ru.msaitov.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msaitov.service.userAccess.UserAccess;
import ru.msaitov.view.UserView;

import java.util.List;

/**
 * Контроллер Мой доступ
 */
@Controller
public class MyAccessController {

    private final UserAccess userAccess;

    private static Logger logger = LogManager.getFormatterLogger(MyAccessController.class);

    @Autowired
    public MyAccessController(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Мой доступ
     *
     * @param userView - получение текущего пользователя
     * @param model - передача параметров в фронт
     * @return переход на страницу myAccess
     */
    @GetMapping("/myAccess")
    public String getMyAccess(@AuthenticationPrincipal UserView userView, Model model) {
        logger.info("[CONTROLLER] method: getMyAccess, Мой доступ");
        List<String> listGaveAccess = userAccess.getListEmailAccess(userView);
        model.addAttribute("listGaveAccess", listGaveAccess);

        List<String> listRequestAccess = userAccess.requestedAccess(userView);
        model.addAttribute("requestedAccess", listRequestAccess);


        return "access/myAccess";
    }

    /**
     * Дать доступ другому пользователю
     *
     * @param gaveAccessValues - получение списка пользователей который нужно дать доступ
     * @param userOwn - получение текущего пользователя
     * @return переход на страницу myAccess
     */
    @PostMapping("/gaveAccessAction")
    public String PostDeniedAccess(@RequestParam List<String> gaveAccessValues,
                                   @AuthenticationPrincipal UserView userOwn) {
        logger.info("[CONTROLLER] method: PostDeniedAccess, Дать доступ другому пользователю");
        userAccess.accessDinied(gaveAccessValues, userOwn);
        return "redirect:/myAccess";
    }

    /**
     * Список всех пользователей
     *
     * @param model - передача параметров в фронт
     * @param userViewExclude - получение текущего пользователя
     * @return переход на страницу listUser
     */
    @GetMapping("/listUser")
    public String getListUser(Model model, @AuthenticationPrincipal UserView userViewExclude) {
        logger.info("[CONTROLLER] method: getListUser, Список всех пользователей");
        List<String> userListEnabledEmail = userAccess.getAllEnabledUser(userViewExclude);
        int userCount = userListEnabledEmail.size();
        model.addAttribute("requestedAccess", userListEnabledEmail);
        model.addAttribute("userCount", userCount);
        return "access/listUser";
    }

    /**
     * Обработка формы: Дал доступ
     *
     * @param requestedAccessValue - список пользователей которые запросили досуп
     * @param userOwn - получение текущего пользователя
     * @param downloadAccess - разрешить скачивание
     * @return переход на страницу myAccess
     */
    @PostMapping("requestAccessAction")
    public String PostRequestAccessAction(@RequestParam List<String> requestedAccessValue,
                                          @AuthenticationPrincipal UserView userOwn,
                                          String downloadAccess) {
        logger.info("[CONTROLLER] method: PostRequestAccessAction, Обработка формы: Дал доступ");
        if (requestedAccessValue == null) {
            return "access/listUser";
        }
        userAccess.addUserAccess(userOwn, requestedAccessValue, downloadAccess);

        return "redirect:/myAccess";
    }

    /**
     * Обработка кнопки: дать доступ
     *
     * @param requestedAccessValues
     * @param userOwn - получение текущего пользователя
     * @return переход на страницу myAccess
     */
    @RequestMapping(value = "requestedAccessAction", method = RequestMethod.POST, params = "giveAccess")
    public String postRequestGiveAccess(@RequestParam List<String> requestedAccessValues,
                                        @AuthenticationPrincipal UserView userOwn) {
        logger.info("[CONTROLLER] method: postRequestGiveAccess, Обработка кнопки: дать доступ");
        userAccess.statusChangeAccessDenied(requestedAccessValues, userOwn, "giveAccess");
        return "redirect:/myAccess";

    }

    /**
     * Обработка кнопки: отказать в доступе
     *
     * @param requestedAccessValues - список пользователей
     * @param userOwn - получение текущего пользователя
     * @return переход на страницу myAccess
     */
    @RequestMapping(value = "requestedAccessAction", method = RequestMethod.POST, params = "denyAccess")
    public String postRequestDenyAccess(@RequestParam List<String> requestedAccessValues,
                                        @AuthenticationPrincipal UserView userOwn) {
        logger.info("[CONTROLLER] method: postRequestDenyAccess, Обработка кнопки: отказать в доступе");
        userAccess.statusChangeAccessDenied(requestedAccessValues, userOwn, "denyAccess");
        return "redirect:/myAccess";
    }
}
