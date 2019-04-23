package ru.msaitov.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.msaitov.model.UserEntity;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-data-repository.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmailTest() {
        UserEntity userEntity = userRepository.findByEmail("slava@mail.ru");

        UserEntity userEntityExpected = new UserEntity();
        userEntityExpected.setEmail("slava@mail.ru");
        userEntityExpected.setVersion(userEntity.getVersion());
        userEntityExpected.setId(3L);
        userEntityExpected.setPassword("$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW");
        userEntityExpected.setEnabled(true);
        userEntityExpected.setRoles(userEntity.getRoles());

        assertThat(userEntityExpected, samePropertyValuesAs(userEntity));

    }


}
