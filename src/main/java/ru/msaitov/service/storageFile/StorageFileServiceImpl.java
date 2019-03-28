package ru.msaitov.service.storageFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.UserRepository;
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
@Transactional
public class StorageFileServiceImpl implements StorageFileService {

    private final UserRepository userRepository;

    private final Mapper mapper;

    @Value("${file.upload-dir}")
    private String pathCommon;

    @Autowired
    public StorageFileServiceImpl(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFiles(List<String> listFiles, UserView userView) {
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
        String idString = userView.getId().toString();
        Path folder = Paths.get(pathCommon, idString).toAbsolutePath().normalize();

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
        return fileList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeFile(MultipartFile file, UserView userView) {

        Path userFolder = getLocationFolder(userView);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = userFolder.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource loadFileAsResource(String fileName, UserView userView) {
        try {
            Path userFolder = getLocationFolder(userView);
            Path filePath = userFolder.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
