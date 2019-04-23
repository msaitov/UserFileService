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
     * @param listFiles - список пользователей
     * @param userView - текущий пользователь
     */
    void deleteFiles(List<String> listFiles, UserView userView);

    /**
     * Получить текущий каталог пользователя
     *
     * @param userView - текущий пользователь
     * @return текущий каталог
     */
    Path getLocationFolder(UserView userView);

    /**
     * Получить список файлов пользователя
     *
     * @param userView - текущий пользователь
     * @return
     */
    List<String> getListFiles(UserView userView);

    /**
     * Сохранить файлы
     *
     * @param file - файл
     * @param userView - текущий пользователь
     * @return список имен файлов
     */
    String storeFile(MultipartFile file, UserView userView);

    /**
     * Загрузить файл
     *
     * @param fileName - имя файла
     * @param userView - текущий пользователь
     * @return имя файла
     */
    Resource loadFileAsResource(String fileName, UserView userView);
}
