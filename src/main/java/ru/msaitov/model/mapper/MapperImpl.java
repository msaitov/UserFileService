package ru.msaitov.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.msaitov.model.UserEntity;
import ru.msaitov.view.UserView;

@Service
public class MapperImpl implements Mapper {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserEntity map(UserView userView) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userView.getId());
        userEntity.setEmail(userView.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userView.getPassword()));
        userEntity.setRoles(userView.getRoles());
        userEntity.setEnabled(userView.getEnabled());
        return userEntity;
    }

    @Override
    public UserView map(UserEntity userEntity) {
        UserView userView = new UserView();
        userView.setId(userEntity.getId());
        userView.setEmail(userEntity.getEmail());
        userView.setPassword(userEntity.getPassword());
        userView.setRoles(userEntity.getRoles());
        userView.setEnabled(userEntity.getEnabled());
        return userView;
    }

}
