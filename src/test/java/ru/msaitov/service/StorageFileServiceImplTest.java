package ru.msaitov.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import ru.msaitov.model.mapper.Mapper;
import ru.msaitov.repository.UserRepository;
import ru.msaitov.service.storageFile.StorageFileServiceImpl;
import ru.msaitov.service.storageFile.exception.FileStorageException;
import ru.msaitov.service.storageFile.exception.MyFileNotFoundException;
import ru.msaitov.view.UserView;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class StorageFileServiceImplTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private Mapper mapper = mock(Mapper.class);
    private StorageFileServiceImpl storageFileService = new StorageFileServiceImpl(userRepository, mapper);
    private StorageFileServiceImpl storageFileServiceSpy = Mockito.spy(storageFileService);

    private UserView userView;

    @Before
    public void setValue() {
        userView = new UserView();
        userView.setEmail("aaa@bbb.ru");

        Path path = mock(Path.class);
        Mockito.doReturn(path).when(storageFileServiceSpy).getLocationFolder(userView);

    }


    @Test
    public void getListFilesTest() {
        List<String> fileList = storageFileServiceSpy.getListFiles(userView);
        assertNull(fileList);
    }

    @Test(expected = FileStorageException.class)
    public void storeFileTest() {
        MultipartFile file = mock(MultipartFile.class);
        storageFileServiceSpy.storeFile(file, userView);
    }

    @Test(expected = MyFileNotFoundException.class)
    public void loadFileAsResource() {
        storageFileServiceSpy.loadFileAsResource("", userView);
    }


}
