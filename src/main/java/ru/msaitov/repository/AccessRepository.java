package ru.msaitov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.msaitov.model.UserAccessEntity;
import ru.msaitov.model.UserEntity;

import java.util.List;

@Repository
public interface AccessRepository extends JpaRepository<UserAccessEntity, Long> {

    @Query("SELECT u.id FROM UserAccessEntity u WHERE u.userOwn.id = :uO AND u.userAccess.id = :uA")
    Long idByUserOwnAndUserAccess(@Param("uO") Long uO, @Param("uA") Long uA);

    @Query("SELECT u FROM UserAccessEntity u WHERE u.userAccess.id = :uA")
    List<UserAccessEntity> filterByUserAccess(@Param("uA") Long uA);

    @Query("SELECT u FROM UserAccessEntity u WHERE u.userOwn.id = :uO")
    List<UserAccessEntity> filterByUserOwn(@Param("uO") Long uO);

    UserAccessEntity findByUserAccess(UserEntity userEntity);

    List<UserAccessEntity> findByUserOwn(UserEntity userEntity);
}
