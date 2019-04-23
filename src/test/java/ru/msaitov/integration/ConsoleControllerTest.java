package ru.msaitov.integration;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("test@mail.ru")
public class ConsoleControllerTest extends General {

    private final String email = "test@mail.ru";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticatedTest() throws Exception {
        this.mockMvc.perform(get("/console"))
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    public void containTest() throws Exception {
        this.mockMvc.perform(get("/console"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Мой доступ")));

    }

    @Test
    public void refreshTest() throws Exception {
        this.mockMvc.perform(post("/refresh"))
                .andDo(print())
                .andExpect(redirectedUrl("/console"));
    }

    @Test
    public void uploadFileTest() throws Exception {

        final String filename = "filename.txt";

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + userRepository.findByEmail(email).getId());
        createUserDir(email);

        MockMultipartFile firstFile = new MockMultipartFile("file", filename, "text/plain", "text test".getBytes());

        this.mockMvc.perform(multipart("/uploadFile")
                .file(firstFile)
        );

        File tempFile = new File(dir + "\\" + filename);
        boolean exists = tempFile.exists();
        assertTrue(exists);
        deleteFolder(dir);
    }

    @Test
    public void uploadMultipleFiles() throws Exception {

        final String filename1 = "filename1.txt";
        final String filename2 = "filename2.txt";

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + userRepository.findByEmail(email).getId());
        createUserDir(email);

        MockMultipartFile firstFile1 = new MockMultipartFile("files", filename1, "text/plain", "text test".getBytes());
        MockMultipartFile firstFile2 = new MockMultipartFile("files", filename2, "text/plain", "text test".getBytes());

        this.mockMvc.perform(multipart("/uploadMultipleFiles")
                .file(firstFile1)
                .file(firstFile2));

        File tempFile = new File(dir + "\\" + filename1);
        boolean exists = tempFile.exists();
        assertTrue(exists);


        tempFile = new File(dir + "\\" + filename2);
        exists = tempFile.exists();
        assertTrue(exists);

        deleteFolder(dir);
    }

    @Test
    public void downloadFileTest() throws Exception {
        final String filename = "filenameDownload.txt";

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + userRepository.findByEmail(email).getId());
        createUserDir(email);

        MockMultipartFile firstFile = new MockMultipartFile("file", filename, "text/plain", "text test".getBytes());

        this.mockMvc.perform(multipart("/uploadFile")
                .file(firstFile)
        );

        this.mockMvc.perform(get("/downloadFile/filenameDownload.txt"));

        deleteFolder(dir);
    }

    @Test
    public void DeleteFilesTest() throws Exception {
        final String filename = "filenameDelete.txt";

        Path folder = Paths.get(pathCommon).toAbsolutePath().normalize();
        File dir = new File(folder + "/" + userRepository.findByEmail(email).getId());
        createUserDir(email);

        MockMultipartFile firstFile = new MockMultipartFile("file", filename, "text/plain", "text test".getBytes());

        this.mockMvc.perform(multipart("/uploadFile")
                .file(firstFile)
        );

        List<String> searchValues = new ArrayList<>();
        searchValues.add(filename);
        this.mockMvc.perform(post("/operationFiles?deleteFls").param("searchValues", filename));

        File tempFile = new File(dir + "\\" + filename);
        boolean exists = tempFile.exists();
        assertTrue(!exists);

        deleteFolder(dir);


    }


}
