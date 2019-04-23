package ru.msaitov.model;

/**
 * Статус доступа между пользователями
 */
public enum StatusAccess {
    ACCESS_ALLOWED_V("Доступ разрешен на просмотр"),
    ACCESS_ALLOWED_VD("Доступ разрешен на просмотр и скачивание"),
    SEND_V("Запрос отправлен на просмотр"),
    SEND_VD("Запрос отправлен на просмотр и скачивание"),
    ACCESS_DENIED("Доступ отказан");

    String status;

    StatusAccess(String statusAccess) {
        this.status = statusAccess;
    }

    /**
     * Получить статус
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }
}
