package ru.msaitov.view;

/**
 * Статистика скачанных файлов
 */
public class ViewStatistic {

    /**
     * Имя файла
     */
    private String fileName;

    /**
     * Количество скачиваний
     */
    private Long downloadCount;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }
}
