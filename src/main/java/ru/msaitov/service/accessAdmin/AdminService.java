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
     * @param userViewExclude - текущий пользователь
     * @return список пользователей
     */
    List<String> getAllEnabledUser(UserView userViewExclude);

    /**
     * Установить список файлов
     *
     * @param listViewUser - список прользователей
     * @param userRequest - текущий пользователь
     */
    void setListFiles(List<String> listViewUser, UserView userRequest);

    /**
     * Получить список файлов
     *
     * @param userRequest - текущий пользователь
     * @return DtoOutListFiles
     */
    DtoOutListFiles getListFiles(UserView userRequest);
}
