package ru.msaitov.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msaitov.service.downloadedStatistic.DownloadedStatisticService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.view.UserView;
import ru.msaitov.view.ViewStatistic;

import java.util.List;

/**
 * Контроллер Аналитик
 */
@Controller
@PreAuthorize("hasAuthority('ANALYST')")
public class AnalystController {

    private final DownloadedStatisticService statisticService;

    private static Logger logger = LogManager.getFormatterLogger(AnalystController.class);

    @Autowired
    public AnalystController(DownloadedStatisticService statisticService) {
        this.statisticService = statisticService;
    }

    /**
     * Обработка кнопки Просмотр статистики
     *
     * @param listUserEmail - получение email пользователей
     * @param userRequest - текущий пользователь который делает запрос
     * @param model - передача параметров в фронт
     * @return переход на страницу statistics
     */
    @RequestMapping(value = "/allListUser", method = RequestMethod.POST, params = "viewStatistics")
    public String postStatistics(@RequestParam List<String> listUserEmail, @AuthenticationPrincipal UserView userRequest, Model model) {
        logger.info("Обработка кнопки Просмотр статистики у пользователей: {}, для авторизированного пользователя: {}", listUserEmail, userRequest.getEmail());
        statisticService.setStatistics(listUserEmail, userRequest);
        return "redirect:/statistics";
    }

    /**
     * Получение статистики пользователя
     *
     * @param userRequest - текущий пользователь который делает запрос
     * @param model - передача параметров в фронт
     * @return переход на страницу statistics
     */
    @GetMapping("/statistics")
    public String getViewStatisticFiles(@AuthenticationPrincipal UserView userRequest,
                                        Model model) {
        DtoOutListFiles dtoOutListFiles = statisticService.getStatistics(userRequest);
        List<ViewStatistic> listFiles = dtoOutListFiles.getListStatistic();
        String email = dtoOutListFiles.getEmail();
        logger.info("Получение статистики для пользователя: {}, у пользователя: {}", userRequest.getEmail(), email);
        model.addAttribute("files", listFiles);
        model.addAttribute("emailOwner", email);
        model.addAttribute("numberOfFiles", listFiles.size());
        return "statistics";
    }

}
