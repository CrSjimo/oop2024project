package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByToken(String token);

    Optional<VerificationToken> findByUser_Id(Long id);

}
