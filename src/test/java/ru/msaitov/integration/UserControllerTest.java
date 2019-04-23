package ru.msaitov.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void redirecLoginTest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(redirectedUrl("/console"));
    }

    @Test
    public void postRegistrationSuccessTest() throws Exception {
        mockMvc.perform(post("/registration").param("email", "test3@mail.ru").param("password", "123"))
                .andDo(print())
                .andExpect(content().string(containsString("На ваш email выслана ссылка для подтверждения регистрации")));
    }

    @Test
    public void postRegistrationUserAlreadyExistsTest() throws Exception {
        mockMvc.perform(post("/registration").param("email", "vasy@mail.ru").param("password", "123"))
                .andDo(print())
                //.andExpect(redirectedUrl("/"))
                .andExpect(content().string(containsString("Регистрация с данным email невозможно, т.к. пользователь уже существует")));
    }


}
