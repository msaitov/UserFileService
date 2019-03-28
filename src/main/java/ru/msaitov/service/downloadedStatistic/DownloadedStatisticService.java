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
     * @param userView
     * @param fileName
     */
    void incDownload(UserView userView, String fileName);

    /**
     * Получить статистику
     *
     * @param userRequest
     * @return
     */
    DtoOutListFiles getStatistics(UserView userRequest);

    /**
     * Установить статистику
     *
     * @param listViewUser
     * @param userView
     */
    void setStatistics(List<String> listViewUser, UserView userView);
}
