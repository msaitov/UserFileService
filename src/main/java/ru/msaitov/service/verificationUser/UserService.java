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
     * @param userView - текущий пользователь
     * @param url - url
     * @throws UserAlreadyExistException - исключение, если пользователь уже существует
     */
    void registerNewUserAccount(UserView userView, String url) throws UserAlreadyExistException;

    /**
     * Получить пользователя по имени
     *
     * @param username - имя пользователя
     * @return UserDetails
     * @throws UsernameNotFoundException - исключение, если пользователь не найден
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
