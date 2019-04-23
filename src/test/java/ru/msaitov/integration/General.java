package ru.msaitov.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.msaitov.repository.UserRepository;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class General {

    @Value("${file.upload-dir}")
    String pathCommon;

    @Autowired
    UserRepository userRepository;


    void createUserDir(String email) {
        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder.toString());
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(folder + "/" + userRepository.findByEmail(email).getId());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
