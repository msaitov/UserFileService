package ru.msaitov.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Доступ между пользователями
 */
@Entity
@Table(schema = "storage_service", name = "user_access")
public class UserAccessEntity {

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
     * Пользователь - владелец
     */
    @OneToOne
    @JoinColumn(name = "id_user_own")
    private UserEntity userOwn;

    /**
     * Пользователь - который запрашивает доступ
     */
    @OneToOne
    @JoinColumn(name = "id_user_access")
    private UserEntity userAccess;

    /**
     * статус запроса
     */
    @Enumerated(EnumType.STRING)
    private StatusAccess statusAccess;

    /**
     * Статус разрешения на скачивания файлов
     */
    private Boolean downloadEnabled;

    /**
     * Конструктор для hibernate
     */
    public UserAccessEntity() {
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

    public UserEntity getUserOwn() {
        return userOwn;
    }

    public void setUserOwn(UserEntity userOwn) {
        this.userOwn = userOwn;
    }

    public UserEntity getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(UserEntity userAccess) {
        this.userAccess = userAccess;
    }

    public StatusAccess getStatusAccess() {
        return statusAccess;
    }

    public void setStatusAccess(StatusAccess statusAccess) {
        this.statusAccess = statusAccess;
    }

    public Boolean getDownloadEnabled() {
        return downloadEnabled;
    }

    public void setDownloadEnabled(Boolean downloadEnabled) {
        this.downloadEnabled = downloadEnabled;
    }
}
