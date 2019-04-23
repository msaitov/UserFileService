package ru.msaitov.service.verificationToken;

/**
 * Сервис токен
 */
public interface VerificationTokenService {

    /**
     * Проверка токена
     *
     * @param token - токен
     * @return состояние токена
     */
    TokenState validateVerificationToken(String token);
}
