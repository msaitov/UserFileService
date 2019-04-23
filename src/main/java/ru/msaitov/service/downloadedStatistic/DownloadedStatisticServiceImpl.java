package ru.msaitov.service.downloadedStatistic;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.model.DownloadedStatisticEntity;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.DownloadedStatisticRepository;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequest;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DownloadedStatisticServiceImpl implements DownloadedStatisticService {

    private final DownloadedStatisticRepository statisticRepository;

    private final UserAccessRequest userAccessRequest;

    private final StorageFileService storageFileService;

    private final Mapper mapper;

    private final UserRepository userRepository;

    private static Logger logger;
    private Map<UserView, DtoOutListFiles> listStatistics;

    @Autowired
    public DownloadedStatisticServiceImpl(DownloadedStatisticRepository statisticRepository, UserAccessRequest userAccessRequest, StorageFileService storageFileService, Mapper mapper, UserRepository userRepository, Logger logger, Map<UserView, DtoOutListFiles> listStatistics) {
        this.statisticRepository = statisticRepository;
        this.userAccessRequest = userAccessRequest;
        this.storageFileService = storageFileService;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.listStatistics = listStatistics;
        DownloadedStatisticServiceImpl.logger = logger;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incDownload(UserView userView, String fileName) {
        logger.info("[SERVICE] incDownload");
        UserEntity userEntity = userRepository.getOne(userView.getId());
        DownloadedStatisticEntity statisticEntity = statisticRepository.findByFilenameAndUserEntity(fileName, userEntity.getId());
        if (statisticEntity != null) {
            Long count = statisticEntity.getDownloadCount();
            count++;
            statisticEntity.setDownloadCount(count);
        } else {
            statisticEntity = new DownloadedStatisticEntity();
            statisticEntity.setFileName(fileName);
            statisticEntity.setUserEntity(userEntity);
            statisticEntity.setDownloadCount(1L);
        }
        statisticRepository.save(statisticEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DtoOutListFiles getStatistics(UserView userRequest) {
        logger.info("[SERVICE] getStatistics");
        DtoOutListFiles dtoOutListFiles = this.listStatistics.get(userRequest);
        return dtoOutListFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatistics(List<String> listViewUser, UserView userView) {
        logger.info("[SERVICE] setStatistics");
        DtoOutListFiles dtoOutListFiles = new DtoOutListFiles();
        dtoOutListFiles.setListStatistic(Collections.emptyList());
        listStatistics.put(userView, dtoOutListFiles);
        UserEntity userOwner = userAccessRequest.getOwner(listViewUser);
        if (userOwner == null) {
            return;
        }
        UserView userViewOwner = mapper.map(userOwner);
        List<String> listFiles = storageFileService.getListFiles(userViewOwner);
        List<DownloadedStatisticEntity> statisticEntities = new ArrayList<>();
        for (String fileName : listFiles) {
            DownloadedStatisticEntity statisticEntity = statisticRepository.findByFilenameAndUserEntity(fileName, userOwner.getId());
            if (statisticEntity == null) {
                statisticEntity = new DownloadedStatisticEntity();
                statisticEntity.setDownloadCount(0L);
                statisticEntity.setFileName(fileName);
            }
            statisticEntities.add(statisticEntity);
        }

        dtoOutListFiles.setListStatistic(statisticEntities);
        dtoOutListFiles.setEmail(userOwner.getEmail());

        this.listStatistics.put(userView, dtoOutListFiles);
    }
}
