package ru.msaitov.service.sendEMail;

import ru.msaitov.view.UserView;

/**
 * Сервис почты
 */
public interface EmailService {

    /**
     * Послать сообщение
     *
     * @param user - текущий пользователь
     * @param url - url
     * @param token - токен
     */
    void send(UserView user, String url, String token);

}
