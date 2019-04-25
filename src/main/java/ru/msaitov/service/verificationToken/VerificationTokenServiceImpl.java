package ru.msaitov.service.verificationToken;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.VerificationToken;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.repository.VerificationTokenRepository;

import java.util.Calendar;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    private final UserRepository userRepository;

    private static Logger logger = LogManager.getFormatterLogger(VerificationTokenServiceImpl.class);

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenState validateVerificationToken(String token) {
        logger.info("Проверка токена: {}", token);
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            logger.info("Токен: {}, недействителен", token);
            return TokenState.TOKEN_INVALID;
        }

        final UserEntity userEntity = verificationToken.getUserEntity();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            userRepository.delete(userEntity);
            logger.info("Токен: {}, время истекло", token);
            return TokenState.TOKEN_EXPIRED;
        }

        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        tokenRepository.delete(verificationToken);
        logger.info("Токен: {}, действителен", token);
        return TokenState.TOKEN_VALID;
    }

}
