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
import ru.msaitov.service.userAccess.UserAccessImpl;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserAccessImplTest extends MockInit {

    private UserRepository userRepository = mock(UserRepository.class);
    private AccessRepository accessRepository = mock(AccessRepository.class);
    private Mapper mapper = mock(Mapper.class);
    private Logger logger = mock(Logger.class);
    private UserAccessImpl userAccess = new UserAccessImpl(userRepository, accessRepository, mapper, logger);
    private UserAccessImpl userAccessSpy = Mockito.spy(userAccess);

    @Test
    public void saveUserTest() {
        userAccess.saveUser(userView);
        verify(userRepository, times(1)).save(any());

    }

    @Test
    public void requestedAccessTest() {

        List<UserAccessEntity> accessEntityList = new ArrayList<>();
        UserAccessEntity userAccessEntity = new UserAccessEntity();
        userAccessEntity.setStatusAccess(StatusAccess.SEND_V);
        userAccessEntity.setUserAccess(userEntity);
        accessEntityList.add(userAccessEntity);

        when(mapper.map((userView))).thenReturn(userEntity);
        when(accessRepository.filterByUserOwn(any())).thenReturn(accessEntityList);
        List<String> emails = userAccess.requestedAccess(userView);

        List<String> emailsExpect = new ArrayList<>();
        emailsExpect.add("ddd@ccc.ru - Запрос отправлен на просмотр");
        assertEquals(emails, emailsExpect);

    }

    @Test
    public void getAllEnabledUserTest() {
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntity.setEnabled(true);
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        UserEntity userEntityExclude = new UserEntity();
        userEntityExclude.setEmail("aaa@mail.ru");
        when(mapper.map(userView)).thenReturn(userEntityExclude);
        when(mapper.map(userEntity)).thenReturn(userView);
        Mockito.doReturn(null).when(accessRepository).idByUserOwnAndUserAccess(any(), any());


        List<String> userViewListEmailExpect = new ArrayList<>();
        userViewListEmailExpect.add("aaa@bbb.ru");
        List<String> userViewListEmail = userAccessSpy.getAllEnabledUser(userView);
        assertEquals(userViewListEmail, userViewListEmailExpect);
    }

    @Test
    public void getAllUser() {
        userAccess.getAllUser();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void addUserAccessTest() {
        List<String> listUserAccess = new ArrayList<>();
        listUserAccess.add("qqq@mail.ru");
        listUserAccess.add("aaa@mail.ru");

        userAccessSpy.addUserAccess(userView, listUserAccess, "true");
        verify(accessRepository, times(1)).save(any());
    }

    @Test
    public void getListEmailAccessTest() {
        UserView userView = new UserView();
        userView.setEmail("ddd@ccc.ru");
        List<UserAccessEntity> listUserAccessEntities = new ArrayList<>();
        userAccessEntity.setStatusAccess(StatusAccess.ACCESS_ALLOWED_V);
        userAccessEntity.setUserAccess(userEntity);
        listUserAccessEntities.add(userAccessEntity);

        when(accessRepository.findAll()).thenReturn(listUserAccessEntities);
        List<String> listUserAccess = userAccessSpy.getListEmailAccess(userView);
        List<String> listUserAccessExpect = new ArrayList<>();
        listUserAccessExpect.add("ddd@ccc.ru - Доступ разрешен на просмотр");
        assertEquals(listUserAccess, listUserAccessExpect);

    }

    @Test
    public void accessDiniedTest() {
        List<String> listDenied = new ArrayList<>();
        listDenied.add("qqq@mail.ru");

        when(mapper.map(userView)).thenReturn(userEntity);
        when(userRepository.findByEmail(any())).thenReturn(userEntity);
        userAccessSpy.accessDinied(listDenied, userView);
        verify(accessRepository, times(1)).deleteById(any());
    }

    @Test
    public void statusChangeAccessDeniedTeste() {
        List<String> requestedEmail = new ArrayList<>();
        requestedEmail.add("qqq@mail.ru");
        when(userRepository.findByEmail(any())).thenReturn(userEntity);
        userAccessEntity.setStatusAccess(StatusAccess.SEND_V);
        Optional<UserAccessEntity> accessEntity = Optional.ofNullable(userAccessEntity);
        when(mapper.map(userView)).thenReturn(userEntity);
        when(accessRepository.findById(any())).thenReturn(accessEntity);

        userAccessSpy.statusChangeAccessDenied(requestedEmail, userView, "giveAccess");

    }

    @Test
    public void getUserViewByIdTest() {
        userAccess.getUserViewById(1L);
        verify(mapper, times(1)).map((UserEntity) any());
    }
}

















