package ru.msaitov.service.accessAdmin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
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
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final UserAccessRequest accessRequest;

    private final StorageFileService storageFileService;

    private final Mapper mapper;

    private static Logger logger = LogManager.getFormatterLogger(AdminServiceImpl.class);

    private Map<UserView, DtoOutListFiles> listFiles;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, UserAccessRequest accessRequest, StorageFileService storageFileService, Mapper mapper, Map<UserView, DtoOutListFiles> listFiles) {
        this.userRepository = userRepository;
        this.accessRequest = accessRequest;
        this.storageFileService = storageFileService;
        this.mapper = mapper;
        this.listFiles = listFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllEnabledUser(UserView userViewExclude) {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<String> userViewListEmail = new ArrayList<>();
        logger.info("Для акканута: {}, Получить список все активированных пользователей.", userViewExclude.getEmail());
        for (UserEntity userEntity : userEntityList) {
            if (!userEntity.getEmail().equals(userViewExclude.getEmail())) {
                userViewListEmail.add(userEntity.getEmail());
            }
        }
        logger.info("Список пользователей: {}", userViewListEmail);
        return userViewListEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setListFiles(List<String> listViewUser, UserView userRequest) {

        DtoOutListFiles dtoOutListFiles = new DtoOutListFiles();
        dtoOutListFiles.setListFiles(Collections.emptyList());
        listFiles.put(userRequest, dtoOutListFiles);
        UserEntity userOwner = accessRequest.getOwner(listViewUser);
        if (userOwner == null) {
            return;
        }
        logger.info("Установить список владельца файлов: {}, для аккаунта: {}", userOwner.getEmail(), userRequest.getEmail());
        UserView userViewOwner = mapper.map(userOwner);
        List<String> listFiles = storageFileService.getListFiles(userViewOwner);
        dtoOutListFiles.setListFiles(listFiles);
        dtoOutListFiles.setEmail(userOwner.getEmail());
        this.listFiles.put(userRequest, dtoOutListFiles);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DtoOutListFiles getListFiles(UserView userRequest) {
        logger.info("Получить список файлов для аккаунта: {}", userRequest.getEmail());
        DtoOutListFiles dtoOutListFiles = this.listFiles.get(userRequest);
        return dtoOutListFiles;
    }

}
