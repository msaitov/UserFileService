package ru.msaitov.repository;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.msaitov.model.UserAccessEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-data-repository.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AccessRepositoryTest {

    @Autowired
    private AccessRepository accessRepository;

    @Test
    public void idByUserOwnAndUserAccessTest() {
        Long id = accessRepository.idByUserOwnAndUserAccess(2L, 1L);
        assertEquals((Long) 2L, id);
    }

    @Test
    public void filterByUserAccessTest() {
        List<UserAccessEntity> accessEntityList = accessRepository.filterByUserAccess(3L);
        List<String> emailListExpected = new ArrayList<>();
        emailListExpected.add("slava@mail.ru");
        for (int i = 0; i < accessEntityList.size(); i++) {
            String email = accessEntityList.get(i).getUserAccess().getEmail();
            String emailExpected = emailListExpected.get(i);
            assertEquals(emailExpected, email);
        }
    }

    @Test
    public void filterByUserOwnTest() {
        List<UserAccessEntity> accessEntityList = accessRepository.filterByUserAccess(2L);
        List<String> emailListExpected = new ArrayList<>();
        emailListExpected.add("dima@mail.ru");
        emailListExpected.add("slava@mail.ru");
        for (int i = 0; i < accessEntityList.size(); i++) {
            String email = accessEntityList.get(i).getUserAccess().getEmail();
            String emailExpected = emailListExpected.get(i);
            assertEquals(emailExpected, email);
        }
    }

}
