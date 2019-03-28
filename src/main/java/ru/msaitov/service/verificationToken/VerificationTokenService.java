package ru.msaitov.service.verificationToken;

/**
 * Сервис токен
 */
public interface VerificationTokenService {

    /**
     * Проверка токена
     *
     * @param token
     * @return
     */
    TokenState validateVerificationToken(String token);
}
