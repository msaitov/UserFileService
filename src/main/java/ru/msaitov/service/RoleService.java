package ru.msaitov.service;

import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.msaitov.model.Role;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.AccessRepository;
import ru.msaitov.repository.UserRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final UserRepository userRepository;

    private final AccessRepository accessRepository;

    private final Mapper mapper;

    @Autowired
    public RoleService(UserRepository userRepository, AccessRepository accessRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
        this.mapper = mapper;
    }

    public void saveRole(UserEntity userEntity, Map<String, String> form) {

        //UserEntity userEntity = userRepository.getOne(userId);
//        userEntity.setVersion(0L);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        PersistentSet roles1 = new PersistentSet();

        //userEntity.setRoles();
        userEntity.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                //userEntity.getRoles().add(Role.valueOf(key));
                roles1.add(Role.valueOf(key));
            }
        }
        userEntity.setRoles(roles1);
        userRepository.save(userEntity);
    }

}
