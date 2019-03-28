package ru.msaitov.service.accessAdmin;

import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.view.UserView;

import java.util.List;

/**
 * Сервис для пользователя с ролью Admin
 */
public interface AdminService {

    /**
     * Получить список все активированныз пользователей
     *
     * @param userViewExclude
     * @return
     */
    List<String> getAllEnabledUser(UserView userViewExclude);

    /**
     * Установить список файлов
     *
     * @param listViewUser
     * @param userRequest
     */
    void setListFiles(List<String> listViewUser, UserView userRequest);

    /**
     * Получить список файлов
     *
     * @param userRequest
     * @return
     */
    DtoOutListFiles getListFiles(UserView userRequest);
}
