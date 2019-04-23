package ru.msaitov.service;

import org.junit.Test;
import ru.msaitov.model.UserEntity;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.accessAdmin.AdminServiceImpl;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequest;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminServiceImplTest extends MockInit {

    private List<String> userViewListEmail = mock(ArrayList.class);
    private Map<UserView, DtoOutListFiles> listFiles = mock(HashMap.class);

    private UserRepository userRepository = mock(UserRepository.class);
    private UserAccessRequest accessRequest = mock(UserAccessRequest.class);
    private StorageFileService storageFileService = mock(StorageFileService.class);
    private AdminServiceImpl adminService = new AdminServiceImpl(userRepository, accessRequest, storageFileService, mapper, listFiles);


    @Test
    public void getAllEnabledUserTest() {

        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);

        List<String> listExpect = new ArrayList<>();
        listExpect.add(userEntity.getEmail());
        List<String> listResult = adminService.getAllEnabledUser(userView);
        assertEquals(listResult, listExpect);
    }


    @Test
    public void setListFilesTest() {
        when(accessRequest.getOwner(any())).thenReturn(userEntity);
        adminService.setListFiles(userViewListEmail, userView);
        verify(listFiles, times(2)).put(any(), any());
    }

    @Test
    public void getListFilesTest() {
        adminService.getListFiles(userView);
        verify(listFiles, times(1)).get(any());

    }

}


