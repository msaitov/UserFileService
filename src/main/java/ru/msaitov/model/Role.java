package ru.msaitov.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Роли
 */
public enum Role implements GrantedAuthority {

    USER, ANALYST, ADMIN;

    /**
     * Получить строковое представление роли
     *
     * @return
     */
    @Override
    public String getAuthority() {
        return name();
    }


}
