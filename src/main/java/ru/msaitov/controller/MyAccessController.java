package ru.msaitov.controller;

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

    @Autowired
    public MyAccessController(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Мой доступ
     *
     * @param userView
     * @param model
     * @return
     */
    @GetMapping("/myAccess")
    public String getMyAccess(@AuthenticationPrincipal UserView userView, Model model) {
        List<String> listGaveAccess = userAccess.getListEmailAccess(userView);
        model.addAttribute("listGaveAccess", listGaveAccess);

        List<String> listRequestAccess = userAccess.requestedAccess(userView);
        model.addAttribute("requestedAccess", listRequestAccess);


        return "access/myAccess";
    }

    /**
     * Дать доступ другому пользователю
     *
     * @param gaveAccessValues
     * @param userOwn
     * @return
     */
    @PostMapping("/gaveAccessAction")
    public String PostDeniedAccess(@RequestParam List<String> gaveAccessValues,
                                   @AuthenticationPrincipal UserView userOwn) {
        userAccess.accessDinied(gaveAccessValues, userOwn);
        return "redirect:/myAccess";
    }

    /**
     * Список всех пользователей
     *
     * @param model
     * @param userViewExclude
     * @return
     */
    @GetMapping("/listUser")
    public String getListUser(Model model, @AuthenticationPrincipal UserView userViewExclude) {
        List<String> userListEnabledEmail = userAccess.getAllEnabledUser(userViewExclude);
        int userCount = userListEnabledEmail.size();
        model.addAttribute("requestedAccess", userListEnabledEmail);
        model.addAttribute("userCount", userCount);
        return "access/listUser";
    }

    /**
     * Обработка формы Запросили доступ
     *
     * @param requestedAccessValue
     * @param userOwn
     * @param downloadAccess
     * @return
     */
    @PostMapping("requestAccessAction")
    public String PostRequestAccessAction(@RequestParam List<String> requestedAccessValue,
                                          @AuthenticationPrincipal UserView userOwn,
                                          String downloadAccess) {
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
     * @param userOwn
     * @return
     */
    @RequestMapping(value = "requestedAccessAction", method = RequestMethod.POST, params = "giveAccess")
    public String postRequestGiveAccess(@RequestParam List<String> requestedAccessValues,
                                        @AuthenticationPrincipal UserView userOwn) {
        userAccess.statusChangeAccessDenied(requestedAccessValues, userOwn, "giveAccess");
        return "redirect:/myAccess";

    }

    /**
     * Обработка кнопки: отказать в доступе
     *
     * @param requestedAccessValues
     * @param userOwn
     * @return
     */
    @RequestMapping(value = "requestedAccessAction", method = RequestMethod.POST, params = "denyAccess")
    public String postRequestDenyAccess(@RequestParam List<String> requestedAccessValues,
                                        @AuthenticationPrincipal UserView userOwn) {
        userAccess.statusChangeAccessDenied(requestedAccessValues, userOwn, "denyAccess");
        return "redirect:/myAccess";

    }
}
