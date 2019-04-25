package ru.msaitov.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.msaitov.model.StatusAccess;
import ru.msaitov.service.downloadedStatistic.DownloadedStatisticService;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequest;
import ru.msaitov.view.UserView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Контроллер Запросить доступ
 */
@Controller
public class RequestController {

    private final UserAccessRequest userAccessRequest;

    private final StorageFileService storageFileService;

    private final DownloadedStatisticService statisticService;

    private static Logger logger = LogManager.getFormatterLogger(RequestController.class);

    @Autowired
    public RequestController(UserAccessRequest userAccessRequest, StorageFileService storageFileService, DownloadedStatisticService statisticService) {
        this.userAccessRequest = userAccessRequest;
        this.storageFileService = storageFileService;
        this.statisticService = statisticService;
    }

    /**
     * Запросить доступ
     *
     * @param userRequest - получение текущего пользователя
     * @param model - передача параметров в фронт
     * @return переход на страницу requestAccess
     */
    @GetMapping("/requestAccess")
    public String getRequestAccess(@AuthenticationPrincipal UserView userRequest, Model model) {
        List<String> ownEmails = userAccessRequest.getAllAccessUser(userRequest);
        logger.info("Пользователь: {}, запрашивает доступ у других пользователей: {}", userRequest.getEmail(), ownEmails);
        model.addAttribute("listUserAccess", ownEmails);
        return "access/requestAccess";
    }


    /**
     * Просмотр файлов другого пользователя
     *
     * @param userRequest - получение текущего пользователя
     * @param model - передача параметров в фронт
     * @return переход на страницу viewFiles
     */
    @GetMapping("/viewFiles")
    public String getViewFiles(@AuthenticationPrincipal UserView userRequest,
                               Model model) {
        DtoOutListFiles dtoOutListFiles = userAccessRequest.getListFiles(userRequest);
        logger.info("Пользователь: {}, просматривает файлы у другого пользователя.", userRequest.getEmail());
        StatusAccess statusAccess = StatusAccess.ACCESS_DENIED;
        List<String> listFiles = null;
        String email = null;
        String id = null;
        if (dtoOutListFiles != null) {
            listFiles = dtoOutListFiles.getListFiles();
            statusAccess = dtoOutListFiles.getStatusAccess();
            email = dtoOutListFiles.getEmail();
            id = dtoOutListFiles.getId().toString();
        }
        if (statusAccess.equals(StatusAccess.ACCESS_DENIED)) {
            model.addAttribute("messageDenied", "Доступ закрыт");
            logger.info("Для Пользователя: {}, доступ на просмотр файлов - закрыт", userRequest.getEmail());
            return "access/viewFiles";
        }
        if (listFiles.isEmpty()) {
            model.addAttribute("messageDenied", "Файлы отсутствуют");
            logger.info("У текущего пользователя: {}, файлы отсутствуют", email);
            return "access/viewFiles";
        }
        if (statusAccess.equals(StatusAccess.ACCESS_ALLOWED_VD)) {
            model.addAttribute("downloadAccess", "true");
        }
        logger.info("Список файлов: {}", listFiles);
        model.addAttribute("listFiles", listFiles);
        model.addAttribute("emailOwner", email);
        model.addAttribute("idOwner", id);

        return "access/viewFiles";
    }


    /**
     * Обработка кнопки: Просмотр файлов
     *
     * @param listUserAccessValues - список файлов
     * @param userRequest - получение текущего пользователя
     * @param model - передача параметров в фронт
     * @return переход на страницу viewFiles
     */
    @RequestMapping(value = "/operationRequest", method = RequestMethod.POST, params = "viewFiles")
    public String postOperationRequest(@RequestParam List<String> listUserAccessValues, @AuthenticationPrincipal UserView userRequest, Model model) {
        logger.info("Обработка кнопки, просмотр файлов пользователя: {}", listUserAccessValues);
        userAccessRequest.userStatusRequested(listUserAccessValues, userRequest);
        return "redirect:/viewFiles";
    }

    /**
     * Список пользователей, для запроса на просмотр или скачивание файлов
     *
     * @param model - передача параметров в фронт
     * @param userEntityExclude - получение текущего пользователя
     * @return переход на страницу listUserRequest
     */
    @GetMapping("/listUserRequest")
    public String getListUserRequest(Model model, @AuthenticationPrincipal UserView userEntityExclude) {
        List<String> userListEnabledEmail = userAccessRequest.getAllEnabledUser(userEntityExclude);
        logger.info("Для аккаунта: {}, вывести список пользователей: {}", userEntityExclude.getEmail(), userListEnabledEmail);
        int userCount = userListEnabledEmail.size();
        model.addAttribute("requestAccessSend", userListEnabledEmail);
        model.addAttribute("userCount", userCount);
        return "access/listUserRequest";
    }


    /**
     * Обработка кнопки: "удалить запрос"
     *
     * @param listUserAccessValues - список пользователей
     * @param userRequest - получение текущего пользователя
     * @return переход на страницу requestAccess
     */
    @RequestMapping(value = "/operationRequest", method = RequestMethod.POST, params = "deleteRequests")
    public String postOperationRequest(@RequestParam List<String> listUserAccessValues,
                                       @AuthenticationPrincipal UserView userRequest) {
        logger.info("Обработка кнопки \"удалить запрос\", аккаунта {}, на удаление запросов: {}", userRequest.getEmail(), listUserAccessValues);
        userAccessRequest.deleteRequest(listUserAccessValues, userRequest);
        return "redirect:/requestAccess";
    }

    /**
     * Обработка формы: сделать запрос
     *
     * @param requestAccessValue - список пользователей
     * @param userRequest - получение текущего пользователя
     * @param downloadAccess - разрешить скачивание
     * @return переход на страницу requestAccess
     */
    @PostMapping("requestAccessSendAction")
    public String PostRequestAccessAction(@RequestParam List<String> requestAccessValue,
                                          @AuthenticationPrincipal UserView userRequest,
                                          String downloadAccess) {
        logger.info("От аккаунта: {}, сделать запрос на доступ у пользователей: {}", userRequest.getEmail(), requestAccessValue);
        if (requestAccessValue == null) {
            return "access/listUser";
        }
        userAccessRequest.sendRequest(requestAccessValue, userRequest, downloadAccess);
        return "redirect:/requestAccess";
    }

    /**
     * Скачать файл у другого пользователя
     *
     * @param fileName - Имя файла
     * @param id - id пользователя
     * @param request - предоставления информации запроса для сервлетов HTTP
     * @return возврат списка объектов ResponseEntity<Resource>
     */
    @GetMapping("/downloadFile/{id}/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 @PathVariable Long id,
                                                 HttpServletRequest request) {
        UserView userView = userAccessRequest.getUserViewById(id);
        logger.info("Скачать файл: {}, у пользователя: {}", fileName, userView.getEmail());
        Resource resource = storageFileService.loadFileAsResource(fileName, userView);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.error("Could not determine file type");
            throw new RuntimeException("Could not determine file type.", ex);
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        statisticService.incDownload(userView, fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
