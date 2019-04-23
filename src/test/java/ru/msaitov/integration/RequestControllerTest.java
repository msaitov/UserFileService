package ru.msaitov.integration;

import org.hamcrest.CoreMatchers;
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

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

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
@WithUserDetails("vasy@mail.ru")
public class RequestControllerTest extends General {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticatedTest() throws Exception {
        this.mockMvc.perform(get("/console"))
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    public void getRequestAccess() throws Exception {
        this.mockMvc.perform(get("/requestAccess"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("request access from the list")));
    }


    @Test
    public void PostRequestAccessActionTest() throws Exception {
        this.mockMvc.perform(post("/requestAccessSendAction")
                .param("requestAccessValue", "analist@mail.ru")
                .param("requestAccessValue", "empty@mail.ru")
        );

        this.mockMvc.perform(get("/requestAccess"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("analist@mail.ru - Запрос отправлен на просмотр ")));
    }

    @Test
    public void getViewFilesTest1() throws Exception {
        this.mockMvc.perform(get("/viewFiles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Доступ закрыт")));
    }

    @Test
    public void getViewFilesTest2() throws Exception {

        final String ownFile = "test@mail.ru";

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + userRepository.findByEmail(ownFile).getId());
        createUserDir(ownFile);

        String filename = "test.txt";
        try (FileWriter writer = new FileWriter(dir + "\\" + filename, false)) {
            String text = "test text";
            writer.write(text);
            writer.flush();
        }

        mockMvc.perform(post("/operationRequest?viewFiles")
                .param("listUserAccessValues", ownFile));


        this.mockMvc.perform(get("/viewFiles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test.txt")));

        deleteFolder(dir);

    }

    @Test
    public void getListUserRequest() throws Exception {
        this.mockMvc.perform(get("/listUserRequest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("analist@mail.ru")))
                .andExpect(content().string(containsString("Количество пользователей: 1")));
    }

    @Test
    public void deleteRequestsTest() throws Exception {

        this.mockMvc.perform(post("/requestAccessSendAction")
                .param("requestAccessValue", "analist@mail.ru")
                .param("requestAccessValue", "empty@mail.ru")
        );

        this.mockMvc.perform(get("/requestAccess"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("analist@mail.ru - Запрос отправлен на просмотр")));

        this.mockMvc.perform(post("/operationRequest?deleteRequests")
                .param("listUserAccessValues", "analist@mail.ru - Запрос отправлен на просмотр"));

        this.mockMvc.perform(get("/requestAccess"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(CoreMatchers.not(containsString("analist@mail.ru - Запрос отправлен на просмотр"))));

    }

    @Test
    public void downloadFileTest() throws Exception {

        final String ownFile = "test@mail.ru";
        Long id = userRepository.findByEmail(ownFile).getId();

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + id);
        createUserDir(ownFile);

        String filename = "test.txt";
        try (FileWriter writer = new FileWriter(dir + "\\" + filename, false)) {
            String text = "test text";
            writer.write(text);
            writer.flush();
        }

        this.mockMvc.perform(get("/downloadFile/" + id + "/test.txt"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(CoreMatchers.not(containsString("analist@mail.ru - Запрос отправлен на просмотр"))));

        deleteFolder(dir);
    }


}
