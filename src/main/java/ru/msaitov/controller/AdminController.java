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
import ru.msaitov.model.Role;
import ru.msaitov.service.accessAdmin.AdminService;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequest;
import ru.msaitov.view.UserView;

import java.util.List;

/**
 * Контроллер Админа
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserAccessRequest userAccessRequest;

    private final AdminService adminService;

    private final StorageFileService storageFileService;

    private static Logger logger = LogManager.getFormatterLogger(AdminController.class);

    @Autowired
    public AdminController(UserAccessRequest userAccessRequest, AdminService adminService, StorageFileService storageFileService) {
        this.userAccessRequest = userAccessRequest;
        this.adminService = adminService;
        this.storageFileService = storageFileService;
    }

    /**
     * Получить список пользователей
     *
     * @param model - передача параметров в фронт
     * @param userView - получение текущего пользователя
     * @return переход на страницу allListUser
     */
    @GetMapping("/allListUser")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ANALYST')")
    public String getAllListUser(Model model, @AuthenticationPrincipal UserView userView) {
        List<String> userListEnabledEmail = adminService.getAllEnabledUser(userView);
        logger.info("Получить список пользователей: {}, для авторизированного пользователя: {}", userListEnabledEmail, userView.getEmail());
        int userCount = userListEnabledEmail.size();
        model.addAttribute("emailList", userListEnabledEmail);
        model.addAttribute("userCount", userCount);
        if (userView.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("isAdminRole", "true");
        }
        return "allListUser";
    }

    /**
     * Обработка списка пользователей
     *
     * @param listUserEmail - получение списка Email
     * @param userRequest - получение текущего пользователя который делает запрос на доступ
     * @param model - передача параметров в фронт
     * @return переход на страницу viewFilesAdmin
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ANALYST')")
    @RequestMapping(value = "/allListUser", method = RequestMethod.POST, params = "viewFiles")
    public String PostAllListUser(@RequestParam List<String> listUserEmail, @AuthenticationPrincipal UserView userRequest, Model model) {
        logger.info("Получение списка email {}, для текущего пользователя который делает запрос {}", listUserEmail, userRequest.getEmail());
        adminService.setListFiles(listUserEmail, userRequest);
        return "redirect:/viewFilesAdmin";
    }

    /**
     * Просмотр файлов
     *
     * @param userRequest - получение текущего пользователя который делает запрос на доступ
     * @param model - передача параметров в фронт
     * @return переход на страницу viewFilesAdmin
     */
    @GetMapping("/viewFilesAdmin")
    public String getViewFiles(@AuthenticationPrincipal UserView userRequest,
                               Model model) {
        DtoOutListFiles dtoOutListFiles = adminService.getListFiles(userRequest);
        List<String> listFiles = dtoOutListFiles.getListFiles();
        String email = dtoOutListFiles.getEmail();
        logger.info("Просмотр файлов пользователя: {}, список файлов: {}", email, listFiles);
        if (listFiles.isEmpty()) {
            logger.info("Файлы пользователя {} - отсутствуют", email);
            model.addAttribute("messageDenied", "Файлы отсутствуют");
            return "viewFilesAdmin";
        }
        model.addAttribute("listFiles", listFiles);
        model.addAttribute("emailOwner", email);
        return "viewFilesAdmin";
    }

    /**
     * Удаление файлов
     *
     * @param ownName      - получение владельца файлов
     * @param searchValues - получить список файлов
     * @return переход на страницу allListUser
     */
    @RequestMapping(value = "/operationFilesAdmin", method = RequestMethod.POST, params = "deleteFls")
    public String postDeleteFiles(@RequestParam String ownName,
                                  @RequestParam List<String> searchValues) {
        logger.info("Удаление файлов владельца: {}, удаляемые файлы: {}", ownName, searchValues);
        if (searchValues != null && searchValues.size() > 0) {
            storageFileService.deleteFiles(searchValues, userAccessRequest.getUserViewByEmail(ownName));
        }
        return "redirect:/allListUser";
    }
}
