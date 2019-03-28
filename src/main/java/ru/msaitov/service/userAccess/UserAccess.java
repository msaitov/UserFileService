package ru.msaitov.service.userAccess;

import ru.msaitov.model.UserEntity;
import ru.msaitov.view.UserView;

import java.util.List;

/**
 * Сервис управления пользователями
 */
public interface UserAccess {

    /**
     * Сохранить пользователя
     *
     * @param userView
     */
    void saveUser(UserView userView);

    /**
     * Получить список все пользоватлей которые сделали запрос
     *
     * @param owner
     * @return
     */
    List<String> requestedAccess(UserView owner);

    /**
     * Получить список всех активных пользователй
     *
     * @param userViewExclude
     * @return
     */
    List<String> getAllEnabledUser(UserView userViewExclude);

    /**
     * Получить список всех пользователей
     *
     * @return
     */
    List<UserEntity> getAllUser();

    /**
     * Добавить пользователя для доступа
     *
     * @param userOwn
     * @param listUserAccess
     * @param downloadAccess
     */
    void addUserAccess(UserView userOwn, List<String> listUserAccess, String downloadAccess);

    /**
     * Получить список всех email пользователя которые сделали запрос
     *
     * @param userOwn
     * @return
     */
    List<String> getListEmailAccess(UserView userOwn);

    /**
     * Отказать в доступе
     *
     * @param listDenied
     * @param userOwn
     */
    void accessDinied(List<String> listDenied, UserView userOwn);

    /**
     * Изменить статус на Доступ запрещен
     *
     * @param requestedEmail
     * @param userOwn
     * @param typeProcess
     */
    void statusChangeAccessDenied(List<String> requestedEmail, UserView userOwn, String typeProcess);

    /**
     * Получить пользователя по Id
     *
     * @param userId
     * @return
     */
    UserView getUserViewById(Long userId);
}
