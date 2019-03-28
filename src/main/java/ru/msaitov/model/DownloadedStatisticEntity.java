package ru.msaitov.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Статистика скачивай файлов
 */
@Entity
@Table(schema = "storage_service", name = "downloaded_statistics")
public class DownloadedStatisticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Служебное поле hibernate
     */
    @Version
    private Long version;

    /**
     * Имя файла
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Количество скачиваний
     */
    @Column(name = "download_count")
    private Long downloadCount;

    /**
     * Пользователь которому пренадлежит файл
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    /**
     * Конструктор для hibernate
     */
    public DownloadedStatisticEntity() {
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
