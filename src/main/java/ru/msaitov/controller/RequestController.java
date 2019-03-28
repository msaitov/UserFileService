package ru.msaitov.controller;

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

    @Autowired
    public RequestController(UserAccessRequest userAccessRequest, StorageFileService storageFileService, DownloadedStatisticService statisticService) {
        this.userAccessRequest = userAccessRequest;
        this.storageFileService = storageFileService;
        this.statisticService = statisticService;
    }

    /**
     * Запросить доступ
     *
     * @param userRequest
     * @param model
     * @return
     */
    @GetMapping("/requestAccess")
    public String getRequestAccess(@AuthenticationPrincipal UserView userRequest, Model model) {
        List<String> ownEmails = userAccessRequest.getAllAccessUser(userRequest);
        model.addAttribute("listUserAccess", ownEmails);
        return "access/requestAccess";
    }


    /**
     * Просмотр файлов другого пользователя
     *
     * @param userRequest
     * @param model
     * @return
     */
    @GetMapping("/viewFiles")
    public String getViewFiles(@AuthenticationPrincipal UserView userRequest,
                               Model model) {
        DtoOutListFiles dtoOutListFiles = userAccessRequest.getListFiles(userRequest);
        List<String> listFiles = dtoOutListFiles.getListFiles();
        StatusAccess statusAccess = dtoOutListFiles.getStatusAccess();
        String email = dtoOutListFiles.getEmail();
        String id = null;
        if (dtoOutListFiles.getId() != null) {
            id = dtoOutListFiles.getId().toString();
        }
        if (statusAccess.equals(StatusAccess.ACCESS_DENIED)) {
            model.addAttribute("messageDenied", "Доступ закрыт");
            return "access/viewFiles";
        }
        if (listFiles.isEmpty()) {
            model.addAttribute("messageDenied", "Файлы отсутствуют");
            return "access/viewFiles";
        }
        if (statusAccess.equals(StatusAccess.ACCESS_ALLOWED_VD)) {
            model.addAttribute("downloadAccess", "true");
        }
        model.addAttribute("listFiles", listFiles);
        model.addAttribute("emailOwner", email);
        model.addAttribute("idOwner", id);

        return "access/viewFiles";
    }


    /**
     * Обработка кнопки: Просмотр файлов
     *
     * @param listUserAccessValues
     * @param userRequest
     * @param model
     * @return
     */
    @RequestMapping(value = "/operationRequest", method = RequestMethod.POST, params = "viewFiles")
    public String postOperationRequest(@RequestParam List<String> listUserAccessValues, @AuthenticationPrincipal UserView userRequest, Model model) {
        userAccessRequest.userStatusRequested(listUserAccessValues, userRequest);
        return "redirect:/viewFiles";
    }

    /**
     * Список пользователей, для запроса на просмотр или скачивание файлов
     *
     * @param model
     * @param userEntityExclude
     * @return
     */
    @GetMapping("/listUserRequest")
    public String getListUserRequest(Model model, @AuthenticationPrincipal UserView userEntityExclude) {
        List<String> userListEnabledEmail = userAccessRequest.getAllEnabledUser(userEntityExclude);
        int userCount = userListEnabledEmail.size();
        model.addAttribute("requestAccessSend", userListEnabledEmail);
        model.addAttribute("userCount", userCount);
        return "access/listUserRequest";
    }


    /**
     * Обработка кнопки: удалить запрос
     *
     * @param listUserAccessValues
     * @param userRequest
     * @return
     */
    @RequestMapping(value = "/operationRequest", method = RequestMethod.POST, params = "deleteRequests")
    public String postOperationRequest(@RequestParam List<String> listUserAccessValues,
                                       @AuthenticationPrincipal UserView userRequest) {
        userAccessRequest.deleteRequest(listUserAccessValues, userRequest);
        return "redirect:/requestAccess";
    }

    /**
     * Обработка формы: сделать запрос
     *
     * @param requestAccessValue
     * @param userRequest
     * @param downloadAccess
     * @return
     */
    @PostMapping("requestAccessSendAction")
    public String PostRequestAccessAction(@RequestParam List<String> requestAccessValue,
                                          @AuthenticationPrincipal UserView userRequest,
                                          String downloadAccess) {
        if (requestAccessValue == null) {
            return "access/listUser";
        }
        userAccessRequest.sendRequest(requestAccessValue, userRequest, downloadAccess);
        return "redirect:/requestAccess";
    }

    /**
     * Скачать файл у другого пользователя
     *
     * @param fileName
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/downloadFile/{id}/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 @PathVariable Long id,
                                                 HttpServletRequest request) {

        UserView userView = userAccessRequest.getUserViewById(id);
        Resource resource = storageFileService.loadFileAsResource(fileName, userView);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new RuntimeException("Could not determine file type.");
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
