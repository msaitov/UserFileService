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
     * @param email
     * @return
     */
    UserView getUserViewByEmail(String email);

    /**
     * Получить пользователя по id
     *
     * @param id
     * @return
     */
    UserView getUserViewById(Long id);

    /**
     * Получить список файлов другого пользователя
     *
     * @param userRequest
     * @return
     */
    DtoOutListFiles getListFiles(UserView userRequest);

    /**
     * Обработка статус доступа
     *
     * @param listViewUser
     * @param userRequest
     */
    void userStatusRequested(List<String> listViewUser, UserView userRequest);

    /**
     * Получить владельца
     *
     * @param listViewUser
     * @return
     */
    UserEntity getOwner(List<String> listViewUser);

    /**
     * Удалить запрос
     *
     * @param listDelete
     * @param userRequest
     */
    void deleteRequest(List<String> listDelete, UserView userRequest);

    /**
     * Получить список всез пользователей которые сделали запрос
     *
     * @param userView
     * @return
     */
    List<String> getAllAccessUser(UserView userView);

    /**
     * Получить список всех активных пользователй
     *
     * @param userViewExclude
     * @return
     */
    List<String> getAllEnabledUser(UserView userViewExclude);

    /**
     * Послать запрос
     *
     * @param owners
     * @param userRequest
     * @param downloadAccess
     */
    void sendRequest(List<String> owners, UserView userRequest, String downloadAccess);
}
