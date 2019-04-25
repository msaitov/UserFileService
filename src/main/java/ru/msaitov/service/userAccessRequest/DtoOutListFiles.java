package ru.msaitov.service.userAccessRequest;

import ru.msaitov.model.DownloadedStatisticEntity;
import ru.msaitov.model.StatusAccess;
import ru.msaitov.view.ViewStatistic;

import java.util.ArrayList;
import java.util.List;

public class DtoOutListFiles {

    private List<String> listFiles;

    private List<DownloadedStatisticEntity> listStatistic;

    private StatusAccess statusAccess;

    private String email;

    private Long id;

    public List<String> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<String> listFiles) {
        this.listFiles = listFiles;
    }

    public StatusAccess getStatusAccess() {
        return statusAccess;
    }

    public void setStatusAccess(StatusAccess statusAccess) {
        this.statusAccess = statusAccess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ViewStatistic> getListStatistic() {
        List<ViewStatistic> viewStatistics = new ArrayList<>();
        for (DownloadedStatisticEntity statisticEntity : listStatistic) {
            ViewStatistic viewStatistic = new ViewStatistic();
            viewStatistic.setFileName(statisticEntity.getFileName());
            viewStatistic.setDownloadCount(statisticEntity.getDownloadCount());
            viewStatistics.add(viewStatistic);
        }
        return viewStatistics;
    }

    public void setListStatistic(List<DownloadedStatisticEntity> listStatistic) {
        this.listStatistic = listStatistic;
    }
}
