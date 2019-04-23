package ru.msaitov.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test@mail.ru")
public class MyAccessControllerTest extends General {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticatedTest() throws Exception {
        this.mockMvc.perform(get("/console"))
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    public void getMyAccessTest() throws Exception {
        this.mockMvc.perform(get("/myAccess"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Мой доступ")));
    }

    @Test
    public void getListUserTest() throws Exception {
        this.mockMvc.perform(get("/listUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("analist@mail.ru")))
                .andExpect(content().string(containsString("Количество пользователей: 1")));
    }

    @Test
    public void PostRequestAccessActionTest() throws Exception {
        this.mockMvc.perform(post("/requestAccessAction")
                .param("requestedAccessValue", "analist@mail.ru")
                .param("requestedAccessValue", "empty@mail.ru")
                .param("downloadAccess", "true"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/myAccess"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("analist@mail.ru - Доступ разрешен на просмотр и скачивание ")));
    }

    @Test
    public void postRequestAccessTest() throws Exception {
        this.mockMvc.perform(post("/requestAccessSendAction")
                .param("requestAccessValue", "test@mail.ru")
                .param("downloadAccess", "true"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }


}
