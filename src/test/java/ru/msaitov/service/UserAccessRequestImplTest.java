package ru.msaitov.service;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import ru.msaitov.model.StatusAccess;
import ru.msaitov.model.UserAccessEntity;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.AccessRepository;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.storageFile.StorageFileService;
import ru.msaitov.service.userAccessRequest.DtoOutListFiles;
import ru.msaitov.service.userAccessRequest.UserAccessRequestImpl;
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

public class UserAccessRequestImplTest extends MockInit {

    private UserRepository userRepository = mock(UserRepository.class);
    private AccessRepository accessRepository = mock(AccessRepository.class);
    private Mapper mapper = mock(Mapper.class);
    private StorageFileService storageFileService = mock(StorageFileService.class);
    private Map<UserView, DtoOutListFiles> listFiles = mock(HashMap.class);
    private Logger logger = mock(Logger.class);
    private UserAccessRequestImpl userAccessRequest = new UserAccessRequestImpl(userRepository, accessRepository, mapper, storageFileService, logger, listFiles);
    private UserAccessRequestImpl userAccessRequestSpy = Mockito.spy(userAccessRequest);

    @Test
    public void getUserViewByEmailTest() {

        userAccessRequest.getUserViewByEmail("email");
        verify(mapper, times(1)).map((UserEntity) any());
    }

    @Test
    public void getUserViewByIdTest() {
        userAccessRequestSpy.getUserViewById(1L);
        verify(mapper, times(1)).map((UserEntity) any());
    }

    @Test
    public void getListFilesTest() {
        userAccessRequestSpy.getListFiles(userView);
        verify(listFiles, times(1)).get(any());
    }

    @Test
    public void userStatusRequestedTest() {
        Mockito.doReturn(userEntity).when(userAccessRequestSpy).getOwner(listViewUser);
        when(mapper.map(userView)).thenReturn(userEntity);
        UserAccessEntity userAccessEntity = new UserAccessEntity();
        userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_VD);
        when(accessRepository.getOne(any())).thenReturn(userAccessEntity);
        userAccessRequestSpy.userStatusRequested(listViewUser, userView);
        verify(listFiles, times(2)).put(any(), any());
    }

    @Test
    public void getOwnerTest() {

        userAccessRequestSpy.getOwner(listViewUser);
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    public void deleteRequestTest() {
        Mockito.doReturn(listViewUser).when(userAccessRequestSpy).clearEmail(any());
        when(userRepository.findByEmail(any())).thenReturn(userEntity);
        when(mapper.map(userView)).thenReturn(userEntity);
        userAccessRequestSpy.deleteRequest(listViewUser, userView);
        verify(accessRepository, times(1)).deleteById(any());
    }

    @Test
    public void getAllAccessUserTest() {
        when(mapper.map(userView)).thenReturn(userEntity);
        userAccessEntity.setUserOwn(userEntity);
        userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_V);
        when(accessRepository.filterByUserAccess(any())).thenReturn(accessEntityList);

        List<String> userOwnEmails = userAccessRequestSpy.getAllAccessUser(userView);
        List<String> userOwnEmailsExpect = new ArrayList<>();
        userOwnEmailsExpect.add("ddd@ccc.ru - Доступ разрешен на просмотр");
        assertEquals(userOwnEmails, userOwnEmailsExpect);
    }

    @Test
    public void getAllEnabledUser() {

        when(userRepository.findAll()).thenReturn(listEntity);
        UserEntity userEntityExclude = new UserEntity();
        userEntityExclude.setEmail("exclude@mail.ru");
        when(mapper.map(userView)).thenReturn(userEntityExclude);
        when(accessRepository.idByUserOwnAndUserAccess(any(), any())).thenReturn(null);
        userEntity.setEnabled(true);
        String testMail = "test@mail.ru";
        userEntity.setEmail(testMail);
        List<String> userViewListEmail = userAccessRequestSpy.getAllEnabledUser(userView);
        List<String> userViewListEmailExpect = new ArrayList<>();
        userViewListEmailExpect.add(testMail);
        assertEquals(userViewListEmail, userViewListEmailExpect);
    }


    @Test
    public void sendRequestTest() {
        listViewUser.add("test@mail.ru");
        userAccessRequest.sendRequest(listViewUser, userView, "true");
        verify(accessRepository, times(1)).save(any());
    }

    @Test
    public void clearEmailTest() {
        String emailExpect = "test@mail.ru";
        String emailTest = "test@mail.ru - test";
        listViewUser.set(0, emailTest);
        List<String> emails = userAccessRequestSpy.clearEmail(listViewUser);
        List<String> emailsExpect = new ArrayList<>();
        emailsExpect.add(emailExpect);
        assertEquals(emails, emailsExpect);
    }
}
