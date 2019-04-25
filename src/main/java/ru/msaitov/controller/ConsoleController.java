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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.msaitov.model.Role;
import ru.msaitov.service.downloadedStatistic.DownloadedStatisticService;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.storageFile.UploadFileResponse;
import ru.msaitov.view.UserView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер главная консоль программы
 */
@Controller
public class ConsoleController {

    private final StorageFileService storageFileService;

    private final DownloadedStatisticService statisticService;

    private static Logger logger = LogManager.getFormatterLogger(ConsoleController.class);

    @Autowired
    public ConsoleController(StorageFileService storageFileService,
                             DownloadedStatisticService statisticService) {
        this.storageFileService = storageFileService;
        this.statisticService = statisticService;
    }

    /**
     * Главная консоль программы
     *
     * @param model - передача параметров в фронт
     * @param userView - получение текущего пользователя
     * @return переход на страницу console
     */
    @GetMapping("/console")
    public String getConsole(Model model, @AuthenticationPrincipal UserView userView) {
        List<String> listFiles = storageFileService.getListFiles(userView);
        logger.info("Вывод главной консоли программы пользователя: {}, со списоком файлов: {}", userView.getEmail(), listFiles);
        model.addAttribute("listFiles", listFiles);
        if (userView.getRoles().contains(Role.ANALYST)) {
            model.addAttribute("role", "analyst");
        }
        if (userView.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("role", "admin");
        }

        return "console";
    }

    /**
     * закачка файла на сервер
     *
     * @param file - текущий файл
     * @param userView - получение текущего пользователя
     * @return возврат объекта UploadFileResponse
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
                                         @AuthenticationPrincipal UserView userView) {
        String fileName = storageFileService.storeFile(file, userView);
        logger.info("Загрузка файла: {} на сервер, пользователя: {}", fileName, userView.getEmail());
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();


        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    /**
     * закачка файлов на сервер
     *
     * @param files - текущие файлы
     * @param userView - получение текущего пользователя
     * @return возврат списка объектов List<UploadFileResponse>
     */
    @PostMapping("/uploadMultipleFiles")
    @ResponseBody
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                        @AuthenticationPrincipal UserView userView) {
        logger.info("Загрузка файлов на сервер пользователя: {}", userView.getEmail());
        List<UploadFileResponse> uploadFileResponses = Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, userView))
                .collect(Collectors.toList());
        return uploadFileResponses;
    }

    /**
     * Обновить страницу
     *
     * @return переход на страницу console
     */
    @PostMapping("/refresh")
    public String refresh() {
        logger.info("Обновить страницу console");
        return "redirect:/console";
    }

    /**
     * Загрузка файлов с сервера
     *
     * @param fileName - имя файла
     * @param request - предоставления информации запроса для сервлетов HTTP
     * @param userView - получение текущего пользователя
     * @return возврат списка объектов ResponseEntity<Resource>
     */
    @GetMapping("/downloadFile/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request,
                                                 @AuthenticationPrincipal UserView userView) {
        logger.info("Загрузка файла: {} с сервера, пользователя: {}", fileName, userView.getEmail());
        Resource resource = storageFileService.loadFileAsResource(fileName, userView);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.error("[CONTROLLER] Could not determine file type");
            throw new RuntimeException("Could not determine file type", ex);
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

    /**
     * Удаления файлов
     *
     * @param searchValues - получить список файлов
     * @param userView - получение текущего пользователя
     * @return переход на страницу console
     */
    @RequestMapping(value = "/operationFiles", method = RequestMethod.POST, params = "deleteFls")
    public String postDeleteFiles(@RequestParam List<String> searchValues,
                                  @AuthenticationPrincipal UserView userView) {
        logger.info("Удаление файлов: {}, пользователя: {}", searchValues, userView.getEmail());
        if (searchValues != null && searchValues.size() > 0) {
            storageFileService.deleteFiles(searchValues, userView);
            return "redirect:/console";
        }
        return "console";
    }

    /**
     * Обработка списка загрузки файлов
     *
     * @return переход на страницу console
     */
    @RequestMapping(value = "/operationFiles", method = RequestMethod.POST, params = "downloadFls")
    public String postDownloadFiles() {
        logger.info("Обработка списка загрузки файлов");
        return "redirect:/console";
    }

}
