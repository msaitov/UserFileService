package ru.msaitov.service.userAccessRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.model.StatusAccess;
import ru.msaitov.model.UserAccessEntity;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.AccessRepository;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserAccessRequestImpl implements UserAccessRequest {

    private final UserRepository userRepository;

    private final AccessRepository accessRepository;

    private final Mapper mapper;

    private final StorageFileService storageFileService;

    private static Logger logger = LogManager.getFormatterLogger(UserAccessRequestImpl.class);

    private Map<UserView, DtoOutListFiles> listFiles;

    @Autowired
    public UserAccessRequestImpl(UserRepository userRepository, AccessRepository accessRepository, Mapper mapper, StorageFileService storageFileService, Map<UserView, DtoOutListFiles> listFiles) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
        this.mapper = mapper;
        this.storageFileService = storageFileService;
        this.listFiles = listFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserView getUserViewByEmail(String email) {
        logger.info("[SERVICE] method: getUserViewByEmail, Получить пользователя по email");
        return mapper.map(userRepository.findByEmail(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserView getUserViewById(Long id) {
        logger.info("[SERVICE] method: getUserViewById, Получить пользователя по id");
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        UserEntity userEntity = null;
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
        }
        return mapper.map(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DtoOutListFiles getListFiles(UserView userRequest) {
        logger.info("[SERVICE] method: getListFiles, Получить список файлов другого пользователя");
        DtoOutListFiles dtoOutListFiles = this.listFiles.get(userRequest);
        return dtoOutListFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userStatusRequested(List<String> listViewUser, UserView userRequest) {
        logger.info("[SERVICE] method: userStatusRequested, Обработка статус доступа");
        DtoOutListFiles dtoOutListFiles = new DtoOutListFiles();
        dtoOutListFiles.setListFiles(Collections.emptyList());
        dtoOutListFiles.setStatusAccess(StatusAccess.ACCESS_DENIED);
        listFiles.put(userRequest, dtoOutListFiles);
        UserEntity userOwner = getOwner(listViewUser);
        if (userOwner == null) {
            return;
        }
        UserEntity userRequestEntity = mapper.map(userRequest);
        Long id = accessRepository.idByUserOwnAndUserAccess(userOwner.getId(), userRequestEntity.getId());
        UserAccessEntity userAccessEntity = accessRepository.getOne(id);
        StatusAccess statusAccess = userAccessEntity.getStatusAccess();
        if (statusAccess.equals(StatusAccess.ACCESS_ALLOWED_V) || statusAccess.equals(StatusAccess.ACCESS_ALLOWED_VD)) {
            UserView userViewOwner = mapper.map(userOwner);
            List<String> listFiles = storageFileService.getListFiles(userViewOwner);
            dtoOutListFiles.setListFiles(listFiles);
            dtoOutListFiles.setStatusAccess(statusAccess);
            dtoOutListFiles.setEmail(userOwner.getEmail());
            dtoOutListFiles.setId(userOwner.getId());
            this.listFiles.put(userRequest, dtoOutListFiles);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getOwner(List<String> listViewUser) {
        logger.info("[SERVICE] method: getOwner, Получить владельца");
        if (listViewUser == null) {
            return null;
        }
        List<String> emails = clearEmail(listViewUser);
        String email = emails.get(0);
        UserEntity userOwner = userRepository.findByEmail(email);
        return userOwner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRequest(List<String> listDelete, UserView userRequest) {
        logger.info("[SERVICE] method: deleteRequest, Удалить запрос");
        List<String> emails = clearEmail(listDelete);
        for (String email : emails) {
            UserEntity userDelete = userRepository.findByEmail(email);
            UserEntity userRequestEntity = mapper.map(userRequest);
            Long id = accessRepository.idByUserOwnAndUserAccess(userDelete.getId(), userRequestEntity.getId());
            accessRepository.deleteById(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllAccessUser(UserView userView) {
        logger.info("[SERVICE] method: getAllAccessUser, Получить список всез пользователей которые сделали запрос");
        UserEntity userAccessRequest = mapper.map(userView);
        List<UserAccessEntity> userAccessEntities = accessRepository.filterByUserAccess(userAccessRequest.getId());
        List<String> userOwnEmails = new ArrayList<>();
        for (UserAccessEntity userAccessEntity : userAccessEntities) {
            String ownEmail = userAccessEntity.getUserOwn().getEmail();
            ownEmail += " - " + userAccessEntity.getStatusAccess().getStatus();
            userOwnEmails.add(ownEmail);
        }
        return userOwnEmails;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllEnabledUser(UserView userViewExclude) {
        logger.info("[SERVICE] method: getAllEnabledUser, Получить список всех активных пользователй");
        List<UserEntity> userEntityList = userRepository.findAll();
        List<String> userViewListEmail = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            UserEntity userEntityExclude = mapper.map(userViewExclude);
            if (accessRepository.idByUserOwnAndUserAccess(userEntity.getId(), userEntityExclude.getId()) != null) {
                continue;
            }
            if (userEntity.getEnabled() && !userEntity.getEmail().equals(userEntityExclude.getEmail())) {

                userViewListEmail.add(userEntity.getEmail());
            }
        }
        return userViewListEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRequest(List<String> owners, UserView userRequest, String downloadAccess) {
        logger.info("[SERVICE] method: sendRequest, Послать запрос");
        if (owners.size() <= 1) {
            return;
        }
        boolean enabledDownload = false;
        if (downloadAccess != null) {
            enabledDownload = true;
        }
        for (int i = 0; i < owners.size() - 1; i++) {

            UserEntity ownerEntity = userRepository.findByEmail(owners.get(i));
            UserAccessEntity userAccessEntity = new UserAccessEntity();
            userAccessEntity.setUserOwn(ownerEntity);
            UserEntity userRequestEntity = userRepository.getOne(userRequest.getId());
            userAccessEntity.setUserAccess(userRequestEntity);
            userAccessEntity.setDownloadEnabled(enabledDownload);
            userAccessEntity.setDownloadEnabled(enabledDownload);
            if (enabledDownload) {
                userAccessEntity.setStatusAccess(StatusAccess.SEND_VD);
            } else {
                userAccessEntity.setStatusAccess(StatusAccess.SEND_V);
            }
            accessRepository.save(userAccessEntity);
        }

    }

    public List<String> clearEmail(List<String> emails) {
        logger.info("[SERVICE] method: clearEmail");
        List<String> emailsClear = new ArrayList<>();
        for (String email : emails) {

            String[] elements = email.split(" ");
            String eMail = elements[0];
            emailsClear.add(eMail);
        }
        return emailsClear;
    }

}
