package ru.msaitov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.msaitov.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}
