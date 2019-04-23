package ru.msaitov.service.downloadedStatistic;

import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.view.UserView;

import java.util.List;

/**
 * Сервис статистика по файлам
 */
public interface DownloadedStatisticService {

    /**
     * Инкремент статистики файла
     *
     * @param userView - текущий пользователь
     * @param fileName - имя файла
     */
    void incDownload(UserView userView, String fileName);

    /**
     * Получить статистику
     *
     * @param userRequest - текущий пользователь
     * @return DtoOutListFiles
     */
    DtoOutListFiles getStatistics(UserView userRequest);

    /**
     * Установить статистику
     *
     * @param listViewUser - список пользователей
     * @param userView - текущий пользователь
     */
    void setStatistics(List<String> listViewUser, UserView userView);
}
