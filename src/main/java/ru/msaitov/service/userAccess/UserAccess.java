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
     * @param userView - текущий пользователь
     */
    void saveUser(UserView userView);

    /**
     * Получить список все пользоватлей которые сделали запрос
     *
     * @param owner - текущий пользователь
     * @return список пользователей
     */
    List<String> requestedAccess(UserView owner);

    /**
     * Получить список всех активных пользователй
     *
     * @param userViewExclude - текущий пользователь
     * @return список пользователей
     */
    List<String> getAllEnabledUser(UserView userViewExclude);

    /**
     * Получить список всех пользователей
     *
     * @return список UserEntity
     */
    List<UserEntity> getAllUser();

    /**
     * Добавить пользователя для доступа
     *
     * @param userOwn - текущий пользователь
     * @param listUserAccess - список пользователей
     * @param downloadAccess - статус доступа на скачивание
     */
    void addUserAccess(UserView userOwn, List<String> listUserAccess, String downloadAccess);

    /**
     * Получить список всех email пользователя которые сделали запрос
     *
     * @param userOwn - текущий пользователь
     * @return список email
     */
    List<String> getListEmailAccess(UserView userOwn);

    /**
     * Отказать в доступе
     *
     * @param listDenied - список пользователей
     * @param userOwn - текущий пользователь
     */
    void accessDinied(List<String> listDenied, UserView userOwn);

    /**
     * Изменить статус доступа
     *
     * @param requestedEmail - список пользователей
     * @param userOwn - текущий пользователь
     * @param typeProcess - статус доступа
     */
    void statusChangeAccessDenied(List<String> requestedEmail, UserView userOwn, String typeProcess);

    /**
     * Получить пользователя по Id
     *
     * @param userId - id пользователя
     * @return UserView
     */
    UserView getUserViewById(Long userId);
}
