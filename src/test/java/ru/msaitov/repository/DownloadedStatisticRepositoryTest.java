package ru.msaitov.repository;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.msaitov.model.DownloadedStatisticEntity;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-data-repository.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DownloadedStatisticRepositoryTest {

    @Autowired
    DownloadedStatisticRepository statisticRepository;

    @Test
    public void findByFilenameAndUserEntityTest() {
        DownloadedStatisticEntity statisticEntity = statisticRepository.findByFilenameAndUserEntity("def.xlsx", 3L);

        assertEquals("def.xlsx", statisticEntity.getFileName());
        assertEquals((Long) 10L, statisticEntity.getDownloadCount());
    }

}
