package ru.msaitov.service.storageFile;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.msaitov.view.UserView;

import java.nio.file.Path;
import java.util.List;

/**
 * Сервис работа с файлами
 */
public interface StorageFileService {

    /**
     * Удалить файлы
     *
     * @param listFiles
     * @param userView
     */
    void deleteFiles(List<String> listFiles, UserView userView);

    /**
     * Получить текущий каталог пользователя
     *
     * @param userView
     * @return
     */
    Path getLocationFolder(UserView userView);

    /**
     * Получить список файлов пользователя
     *
     * @param userView
     * @return
     */
    List<String> getListFiles(UserView userView);

    /**
     * Сохранить файлы
     *
     * @param file
     * @param userView
     * @return
     */
    String storeFile(MultipartFile file, UserView userView);

    /**
     * Загрузить файл
     *
     * @param fileName
     * @param userView
     * @return
     */
    Resource loadFileAsResource(String fileName, UserView userView);
}
