package ru.msaitov.service.userAccess;

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
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserAccessImpl implements UserAccess {

    private final UserRepository userRepository;

    private final AccessRepository accessRepository;

    private final Mapper mapper;

    private static Logger logger = LogManager.getFormatterLogger(UserAccessImpl.class);

    @Autowired
    public UserAccessImpl(UserRepository userRepository, AccessRepository accessRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUser(UserView userView) {
        logger.info("Сохранить пользователя: {}", userView.getEmail());
        UserEntity userEntity = userRepository.getOne(userView.getId());
        userRepository.save(userEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> requestedAccess(UserView owner) {
        logger.info("Получить для аккаунта: {}, список все пользователей которые сделали запрос", owner.getEmail());
        UserEntity ownerEntity = mapper.map(owner);
        List<UserAccessEntity> userAEs = accessRepository.filterByUserOwn(ownerEntity.getId());
        List<String> emails = new ArrayList<>();
        for (UserAccessEntity userAE : userAEs) {
            if (userAE.getStatusAccess() == StatusAccess.SEND_V ||
                    userAE.getStatusAccess() == StatusAccess.SEND_VD) {

                String strOut = userAE.getUserAccess().getEmail();
                strOut += " - " + userAE.getStatusAccess().getStatus();
                emails.add(strOut);
            }
        }
        logger.info("Список пользователей: {}", emails);
        return emails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllEnabledUser(UserView userViewExclude) {
        logger.info("Получить для аккаунта: {}, список всех активированных пользователей", userViewExclude.getEmail());
        List<UserEntity> userEntityList = userRepository.findAll();
        List<String> userViewListEmail = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            UserEntity userEntityExclude = mapper.map(userViewExclude);
            if (accessRepository.idByUserOwnAndUserAccess(userEntityExclude.getId(), userEntity.getId()) != null) {
                continue;
            }
            if (userEntity.getEnabled() && !userEntity.getEmail().equals(userEntityExclude.getEmail())) {
                userViewListEmail.add(mapper.map(userEntity).getEmail());
            }
        }
        logger.info("Список пользователей: {}", userViewListEmail);
        return userViewListEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEntity> getAllUser() {
        logger.info("Получить UserEntity всех пользователей");
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUserAccess(UserView userOwn, List<String> listUserAccess, String downloadAccess) {
        logger.info("Аккаунт: {}, дает доступ для пользователей: {}", userOwn.getEmail(), listUserAccess);
        if (listUserAccess.size() <= 1) {
            return;
        }
        boolean enabledDownload = false;
        if (downloadAccess != null) {
            enabledDownload = true;
        }
        for (int i = 0; i < listUserAccess.size() - 1; i++) {
            UserAccessEntity userAccessEntity = new UserAccessEntity();
            UserEntity userEntity = userRepository.findByEmail(listUserAccess.get(i));
            UserEntity ownEntity = userRepository.getOne(userOwn.getId());
            userAccessEntity.setUserOwn(ownEntity);
            userAccessEntity.setUserAccess(userEntity);
            userAccessEntity.setDownloadEnabled(enabledDownload);
            if (enabledDownload) {
                userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_VD);
            } else {
                userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_V);
            }
            accessRepository.save(userAccessEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListEmailAccess(UserView userOwn) {
        logger.info("Для акаунта: {}, получить список всех email пользователя которые сделали запрос", userOwn.getEmail());
        List<UserAccessEntity> listUserAccessEntities = accessRepository.findAll();
        List<String> listUserAccess = new ArrayList<>();

        for (UserAccessEntity listUserAccessEntity : listUserAccessEntities) {

            if (listUserAccessEntity.getUserOwn().getEmail().equals(userOwn.getEmail()) &&
                    (listUserAccessEntity.getStatusAccess().equals(StatusAccess.ACCESS_ALLOWED_V) ||
                            listUserAccessEntity.getStatusAccess().equals(StatusAccess.ACCESS_ALLOWED_VD))) {
                String userAccessStr = listUserAccessEntity.getUserAccess().getEmail();
                userAccessStr += " - " + listUserAccessEntity.getStatusAccess().getStatus();
                listUserAccess.add(userAccessStr);
            }
        }
        logger.info("Список пользователей: {}", listUserAccess);
        return listUserAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accessDinied(List<String> listDenied, UserView userOwn) {
        logger.info("Аккаунт: {}, отказывает в доступе для пользователей: {}", userOwn.getEmail(), listDenied);
        for (String s : listDenied) {
            String[] elements = s.split(" ");
            String eMail = elements[0];
            UserEntity userDenide = userRepository.findByEmail(eMail);
            UserEntity ownEntity = mapper.map(userOwn);
            Long id = accessRepository.idByUserOwnAndUserAccess(ownEntity.getId(), userDenide.getId());
            accessRepository.deleteById(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void statusChangeAccessDenied(List<String> requestedEmail, UserView userOwn, String typeProcess) {
        logger.info("Аккаент: {}, изменяет статус доступа для пользователей {}, на {}", userOwn.getEmail(), requestedEmail, typeProcess);
        List<String> emails = clearEmail(requestedEmail);

        for (String email : emails) {
            UserEntity userEntity = userRepository.findByEmail(email);
            UserEntity ownEntity = mapper.map(userOwn);
            Long id = accessRepository.idByUserOwnAndUserAccess(ownEntity.getId(), userEntity.getId());
            if (id == null) {
                return;
            }
            Optional<UserAccessEntity> accessEntity = accessRepository.findById(id);
            UserAccessEntity userAccessEntity = null;
            if (accessEntity.isPresent()) {
                userAccessEntity = accessEntity.get();
            }
            if (userAccessEntity != null && typeProcess.equals("denyAccess")) {
                userAccessEntity.setStatusAccess(StatusAccess.ACCESS_DENIED);
                return;
            }
            if (userAccessEntity != null && typeProcess.equals("giveAccess")) {
                if (userAccessEntity.getStatusAccess() == StatusAccess.SEND_V) {
                    userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_V);
                    return;
                }
                if (userAccessEntity.getStatusAccess() == StatusAccess.SEND_VD) {
                    userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_VD);
                    return;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserView getUserViewById(Long userId) {
        logger.info("Получить пользователя по Id = {}", userId);
        return mapper.map(userRepository.getOne(userId));
    }

    private List<String> clearEmail(List<String> emails) {
        List<String> emailsClear = new ArrayList<>();
        for (String email : emails) {

            String[] elements = email.split(" ");
            String eMail = elements[0];
            emailsClear.add(eMail);
        }
        return emailsClear;
    }

}
