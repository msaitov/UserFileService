package ru.msaitov.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.msaitov.model.VerificationToken;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-data-repository.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VerificationTokenRepositoryTest {

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Test
    public void findByTokenTest() {

        VerificationToken verificationToken = tokenRepository.findByToken("abb341e1-602e-4278-9f28-b3386732f2d9");
        String email = verificationToken.getUserEntity().getEmail();
        assertEquals("vasy@mail.ru", email);
    }


}
