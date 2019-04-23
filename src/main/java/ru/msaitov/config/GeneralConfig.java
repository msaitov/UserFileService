package ru.msaitov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.view.UserView;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GeneralConfig {

    /**
     * Бин HashMap
     *
     * @return new HashMap
     */
    @Bean
    @Scope("prototype")
    public Map<UserView, DtoOutListFiles> typeMap() {
        return new HashMap<>();
    }

}
