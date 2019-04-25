package ru.msaitov.service.storageFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.msaitov.service.storageFile.exception.FileStorageException;
import ru.msaitov.service.storageFile.exception.MyFileNotFoundException;
import ru.msaitov.view.UserView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageFileServiceImpl implements StorageFileService {

    private static Logger logger = LogManager.getFormatterLogger(StorageFileServiceImpl.class);

    @Value("${file.upload-dir}")
    private String pathCommon;

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFiles(List<String> listFiles, UserView userView) {
        logger.info("Для аккаунта: {}, удалить список файлов: {}", userView.getEmail(), listFiles);
        for (String listFile : listFiles) {
            File file = new File(getLocationFolder(userView) + "\\" + listFile);
            file.delete();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getLocationFolder(UserView userView) {
        logger.info("Получить текущий каталог пользователя: {}", userView.getEmail());
        String idString = userView.getId().toString();
        Path folder = Paths.get(pathCommon, idString).toAbsolutePath().normalize();
        logger.info("Текущий каталог: {}", folder.getFileName());
        File dir = new File(folder.toString());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return folder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListFiles(UserView userView) {
        logger.info("Получить список файлов пользователя: {}", userView.getEmail());
        Path userFolder = getLocationFolder(userView);
        File folder = new File(userFolder.toString());
        File[] arrayOfFiles = folder.listFiles();
        List<String> fileList = null;
        if (arrayOfFiles != null) {
            fileList = new ArrayList<>();
            for (File arrayOfFile : arrayOfFiles) {
                fileList.add(arrayOfFile.getName());
            }
        }
        logger.info("Список файлов: {}", fileList);
        return fileList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeFile(MultipartFile file, UserView userView) {
        Path userFolder = getLocationFolder(userView);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        logger.info("Сохранить файл пользователя: {}, имя файла: {}", userView.getEmail(), fileName);
        if (fileName == null || fileName.equals("")) {
            logger.error("Sorry! File not found");
            throw new FileStorageException("Sorry! File not found");
        }
        try {
            if (fileName.contains("..")) {
                logger.error("Sorry! Filename contains invalid path sequence " + fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = userFolder.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            logger.error("Could not store file " + fileName + ". Please try again!", ex);
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource loadFileAsResource(String fileName, UserView userView) {
        logger.info("Загрузить файл пользователя: {}, имя файла: {}", userView.getEmail(), fileName);
        try {
            Path userFolder = getLocationFolder(userView);
            if (fileName == null || fileName.equals("")) {
                logger.error("File not found " + fileName);
                throw new MyFileNotFoundException("[SERVICE] File not found ");
            }
            Path filePath = userFolder.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                logger.error("File not found " + fileName);
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            logger.error("File not found " + fileName, ex);
            throw new MyFileNotFoundException("[SERVICE] File not found " + fileName, ex);
        }
    }
}
