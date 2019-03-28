package ru.msaitov.service.storageFile;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.annotation.Transactional;

@ConfigurationProperties(prefix = "file")
@Transactional
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
