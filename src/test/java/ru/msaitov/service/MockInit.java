package ru.msaitov.service;

import org.junit.Before;
import ru.msaitov.model.UserAccessEntity;
import ru.msaitov.model.UserEntity;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.view.UserView;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class MockInit {

    Mapper mapper = mock(Mapper.class);
    UserEntity userEntity;
    UserAccessEntity userAccessEntity;
    List<UserAccessEntity> accessEntityList;
    UserView userView;
    List<String> listViewUser;
    List<UserEntity> listEntity;

    @Before
    public void setValue() {

        userEntity = new UserEntity();
        userEntity.setEmail("ddd@ccc.ru");
        listEntity = new ArrayList<>();
        listEntity.add(userEntity);

        userAccessEntity = new UserAccessEntity();
        userAccessEntity.setUserOwn(userEntity);

        accessEntityList = new ArrayList<>();
        accessEntityList.add(userAccessEntity);

        userView = new UserView();
        userView.setEmail("aaa@bbb.ru");

        listViewUser = new ArrayList<>();
        listViewUser.add("aaa@mail.ru");
    }

}
