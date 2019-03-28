package ru.msaitov.controller;

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

    @Autowired
    public AdminController(UserAccessRequest userAccessRequest, AdminService adminService, StorageFileService storageFileService) {
        this.userAccessRequest = userAccessRequest;
        this.adminService = adminService;
        this.storageFileService = storageFileService;
    }

    /**
     * Получить список всех пользователей
     *
     * @param model
     * @param userView
     * @return
     */
    @GetMapping("/allListUser")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ANALYST')")
    public String getAllListUser(Model model, @AuthenticationPrincipal UserView userView) {
        List<String> userListEnabledEmail = adminService.getAllEnabledUser(userView);
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
     * @param listUserEmail
     * @param userRequest
     * @param model
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ANALYST')")
    @RequestMapping(value = "/allListUser", method = RequestMethod.POST, params = "viewFiles")
    public String PostAllListUser(@RequestParam List<String> listUserEmail, @AuthenticationPrincipal UserView userRequest, Model model) {
        adminService.setListFiles(listUserEmail, userRequest);
        return "redirect:/viewFilesAdmin";
    }

    /**
     * Просмотр файлов
     *
     * @param userRequest
     * @param model
     * @return
     */
    @GetMapping("/viewFilesAdmin")
    public String getViewFiles(@AuthenticationPrincipal UserView userRequest,
                               Model model) {
        DtoOutListFiles dtoOutListFiles = adminService.getListFiles(userRequest);
        List<String> listFiles = dtoOutListFiles.getListFiles();
        String email = dtoOutListFiles.getEmail();
        if (listFiles.isEmpty()) {
            model.addAttribute("messageDenied", "Файлы отсутствуют");
            return "viewFilesAdmin";
        }
        model.addAttribute("listFiles", listFiles);
        model.addAttribute("emailOwner", email);
        return "viewFilesAdmin";
    }

    @RequestMapping(value = "/operationFilesAdmin", method = RequestMethod.POST,
            params = "deleteFls")
    public String postDeleteFiles(@RequestParam String ownName,
                                  @RequestParam List<String> searchValues) {

        if (searchValues != null && searchValues.size() > 0) {
            storageFileService.deleteFiles(searchValues, userAccessRequest.getUserViewByEmail(ownName));
        }
        return "redirect:/allListUser";
    }
}
