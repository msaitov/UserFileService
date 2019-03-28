package ru.msaitov.service.verificationUser;

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
        if (emailExists(userView.getEmail())) {
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
        UserEntity userEntity = userRepository.findByEmail(username);
        UserView userView = mapper.map(userEntity);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userView;
    }

    private void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    private void saveToken(String token, UserEntity userEntity) {
        final VerificationToken myToken = new VerificationToken(token, userEntity);
        tokenRepository.save(myToken);
    }

    private void sendEmail(UserView userView, String url, String token) {
        emailService.send(userView, url, token);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
}