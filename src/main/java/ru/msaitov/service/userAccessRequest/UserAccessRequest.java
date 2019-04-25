package ru.msaitov.service.userAccessRequest;

import ru.msaitov.model.UserEntity;
import ru.msaitov.view.UserView;

import java.util.List;


/**
 * Сервис доступа между пользователями
 */
public interface UserAccessRequest {

    /**
     * Получить пользователя по email
     *
     * @param email - email пользователя
     * @return UserView
     */
    UserView getUserViewByEmail(String email);

    /**
     * Получить пользователя по id
     *
     * @param id - id пользователя
     * @return UserView
     */
    UserView getUserViewById(Long id);

    /**
     * Получить список файлов другого пользователя
     *
     * @param userRequest - текущий пользователь
     * @return DtoOutListFiles
     */
    DtoOutListFiles getListFiles(UserView userRequest);

    /**
     * Обработка статус доступа и формирование списка файлов другого пользователя
     *
     * @param listViewUser - список пользователей
     * @param userRequest - текущий пользователь
     */
    void userStatusRequested(List<String> listViewUser, UserView userRequest);

    /**
     * Получить владельца
     *
     * @param listViewUser - список пользователей
     * @return UserEntity
     */
    UserEntity getOwner(List<String> listViewUser);

    /**
     * Удалить запрос
     *
     * @param listDelete - список пользователей
     * @param userRequest - текущий пользователь
     */
    void deleteRequest(List<String> listDelete, UserView userRequest);

    /**
     * Получить список всез пользователей которые сделали запрос
     *
     * @param userView - текущий пользователь
     * @return список пользователей
     */
    List<String> getAllAccessUser(UserView userView);

    /**
     * Получить список всех активных пользователй
     *
     * @param userViewExclude - текущий пользователь
     * @return список пользователей
     */
    List<String> getAllEnabledUser(UserView userViewExclude);

    /**
     * Послать запрос
     *
     * @param owners - список пользователей владельцев аккаунта
     * @param userRequest - текущий пользователь
     * @param downloadAccess - статус доступа на скачивание
     */
    void sendRequest(List<String> owners, UserView userRequest, String downloadAccess);
}
