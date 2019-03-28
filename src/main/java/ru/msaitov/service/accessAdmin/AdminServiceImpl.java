package ru.msaitov.service.accessAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequest;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final UserAccessRequest accessRequest;

    private final StorageFileService storageFileService;

    private final Mapper mapper;

    private Map<UserView, DtoOutListFiles> listFiles = new HashMap<>();

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, UserAccessRequest accessRequest, StorageFileService storageFileService, Mapper mapper) {
        this.userRepository = userRepository;
        this.accessRequest = accessRequest;
        this.storageFileService = storageFileService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllEnabledUser(UserView userViewExclude) {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<String> userViewListEmail = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            if (!userEntity.getEmail().equals(userViewExclude.getEmail())) {
                userViewListEmail.add(userEntity.getEmail());
            }
        }
        return userViewListEmail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setListFiles(List<String> listViewUser, UserView userRequest) {
        DtoOutListFiles dtoOutListFiles = new DtoOutListFiles();
        dtoOutListFiles.setListFiles(Collections.emptyList());
        listFiles.put(userRequest, dtoOutListFiles);
        UserEntity userOwner = accessRequest.getOwner(listViewUser);
        if (userOwner == null) {
            return;
        }

        UserView userViewOwner = mapper.map(userOwner);
        List<String> listFiles = storageFileService.getListFiles(userViewOwner);
        dtoOutListFiles.setListFiles(listFiles);
        dtoOutListFiles.setEmail(userOwner.getEmail());
        this.listFiles.put(userRequest, dtoOutListFiles);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DtoOutListFiles getListFiles(UserView userRequest) {
        DtoOutListFiles dtoOutListFiles = this.listFiles.get(userRequest);
        return dtoOutListFiles;
    }


}
