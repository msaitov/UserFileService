package ru.msaitov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.msaitov.model.DownloadedStatisticEntity;

@Repository
public interface DownloadedStatisticRepository extends JpaRepository<DownloadedStatisticEntity, Long> {

    @Query("SELECT d FROM DownloadedStatisticEntity d WHERE d.fileName = :fileName AND d.userEntity.id = :userEntityId")
    DownloadedStatisticEntity findByFilenameAndUserEntity(@Param("fileName") String fileName, @Param("userEntityId") Long userEntityId);
}
