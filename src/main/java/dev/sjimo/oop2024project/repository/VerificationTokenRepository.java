package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByUser_Id(Long userId);

    void deleteByUser_Id(Long userId);
}
