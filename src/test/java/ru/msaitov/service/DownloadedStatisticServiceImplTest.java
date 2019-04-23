package ru.msaitov.service;

import org.junit.Before;
import org.junit.Test;
import ru.msaitov.model.DownloadedStatisticEntity;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.DownloadedStatisticRepository;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.downloadedStatistic.DownloadedStatisticServiceImpl;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequest;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DownloadedStatisticServiceImplTest {

    private DownloadedStatisticRepository statisticRepository = mock(DownloadedStatisticRepository.class);
    private UserAccessRequest userAccessRequest = mock(UserAccessRequest.class);
    private StorageFileService storageFileService = mock(StorageFileService.class);
    private Mapper mapper = mock(Mapper.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private Map<UserView, DtoOutListFiles> listStatistics = mock(HashMap.class);
    private DownloadedStatisticServiceImpl statisticService = new DownloadedStatisticServiceImpl(statisticRepository, userAccessRequest, storageFileService, mapper, userRepository, listStatistics);
    private List<String> listViewUser = mock(ArrayList.class);

    private UserEntity userEntity;
    private UserView userView;
    private DownloadedStatisticEntity downloadedStatisticEntity = mock(DownloadedStatisticEntity.class);


    @Before
    public void setValue() {

        userEntity = new UserEntity();
        userEntity.setEmail("zzz@qqq.ru");

        userView = new UserView();
        userView.setEmail("vvv@eee.ru");

    }

    @Test
    public void incDownloadTest() {
        when(userRepository.getOne(any())).thenReturn(userEntity);
        when(statisticRepository.findByFilenameAndUserEntity(any(), any())).thenReturn(downloadedStatisticEntity);
        statisticService.incDownload(userView, "filename");
        verify(statisticRepository, times(1)).save(any());
    }

    @Test
    public void getStatisticsTest() {
        statisticService.getStatistics(userView);
        verify(listStatistics, times(1)).get(any());
    }

    @Test
    public void setStatisticsTest() {

        when(userAccessRequest.getOwner(any())).thenReturn(userEntity);
        statisticService.setStatistics(listViewUser, userView);
        verify(listStatistics, times(2)).put(any(), any());
    }


}
