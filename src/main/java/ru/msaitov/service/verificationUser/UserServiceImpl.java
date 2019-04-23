package ru.msaitov.service.verificationUser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.controller.exception.UserAlreadyExistException;
import ru.msaitov.model.Role;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.VerificationToken;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.repository.VerificationTokenRepository;
import ru.msaitov.service.sendEMail.EmailService;
import ru.msaitov.view.UserView;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final VerificationTokenRepository tokenRepository;

    private final Mapper mapper;

    private static Logger logger = LogManager.getFormatterLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(EmailService emailService, UserRepository userRepository, VerificationTokenRepository tokenRepository, Mapper mapper) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerNewUserAccount(final UserView userView, final String url) throws UserAlreadyExistException {
        logger.info("[SERVICE] method: registerNewUserAccount, Регистрация нового пользователя");
        if (emailExists(userView.getEmail())) {
            logger.error("User Already Exist");
            throw new UserAlreadyExistException();
        }

        UserEntity userEntity = mapper.map(userView);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        userEntity.setRoles(roles);
        final String token = UUID.randomUUID().toString();

        saveUser(userEntity);
        saveToken(token, userEntity);
        sendEmail(userView, url, token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("[SERVICE] method: loadUserByUsername, Получить пользователя по имени");
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) {
            logger.error("User not found");
            throw new UsernameNotFoundException("User not found");

        }
        UserView userView = mapper.map(userEntity);
        return userView;
    }

    void saveUser(UserEntity userEntity) {
        logger.info("[SERVICE] method: saveUser, сохранить пользователя");
        userRepository.save(userEntity);
    }

    private void saveToken(String token, UserEntity userEntity) {
        logger.info("[SERVICE] method: saveToken, сохранить токен");
        final VerificationToken myToken = new VerificationToken(token, userEntity);
        tokenRepository.save(myToken);
    }

    private void sendEmail(UserView userView, String url, String token) {
        logger.info("[SERVICE] method: sendEmail, послать email");
        emailService.send(userView, url, token);
    }

    private boolean emailExists(final String email) {
        logger.info("[SERVICE] method: emailExists, проверка существования email");
        return userRepository.findByEmail(email) != null;
    }
}