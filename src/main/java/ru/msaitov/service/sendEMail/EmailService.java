package ru.msaitov.service.sendEMail;

import ru.msaitov.view.UserView;

/**
 * Сервис почты
 */
public interface EmailService {

    /**
     * Послать сообщение
     *
     * @param user
     * @param url
     * @param token
     */
    void send(UserView user, String url, String token);

}
