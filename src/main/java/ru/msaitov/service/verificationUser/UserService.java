package ru.msaitov.service.verificationUser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.msaitov.controller.exception.UserAlreadyExistException;
import ru.msaitov.view.UserView;

/**
 * Сервис Пользователь
 */
public interface UserService extends UserDetailsService {

    /**
     * Регистрация нового пользователя
     *
     * @param userView
     * @param url
     * @throws UserAlreadyExistException
     */
    void registerNewUserAccount(UserView userView, String url) throws UserAlreadyExistException;

    /**
     * Получить пользователя по имени
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
