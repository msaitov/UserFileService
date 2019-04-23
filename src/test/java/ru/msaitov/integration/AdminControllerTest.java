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

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertTrue;
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
public class AdminControllerTest extends General {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticatedTest() throws Exception {
        this.mockMvc.perform(get("/console"))
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    public void getAllListUserTest() throws Exception {
        this.mockMvc.perform(get("/allListUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("vasy@mail.ru")));

    }

    @Test
    public void getViewFilesTest() throws Exception {
        this.mockMvc.perform(post("/allListUser?viewFiles").param("listUserEmail", "vasy@mail.ru"));
        this.mockMvc.perform(get("/viewFilesAdmin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Список файлов - доступ Администратора")));

    }


    @Test
    public void postDeleteFilesTest() throws Exception {

        final String ownFile = "vasy@mail.ru";

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + userRepository.findByEmail(ownFile).getId());
        createUserDir(ownFile);

        String filename = "test.txt";
        try (FileWriter writer = new FileWriter(dir + "\\" + filename, false)) {
            String text = "test text";
            writer.write(text);
            writer.flush();
        }

        this.mockMvc.perform(post("/operationFilesAdmin?deleteFls").param("ownName", ownFile).param("searchValues", filename));

        File tempFile = new File(dir + "\\" + filename);
        boolean exists = tempFile.exists();
        assertTrue(!exists);

        //deleteFolder(dir);


    }


}
