package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.ChatToMemberCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatToMemberCandidateRepository extends JpaRepository<ChatToMemberCandidate, Long> {
    boolean existsByUser_IdAndChat_IdAndStatus(Long userId, Long chatId, ChatToMemberCandidate.Status status);

    Optional<ChatToMemberCandidate> findByUser_IdAndChat_Id(Long userId, Long chatId);

    List<ChatToMemberCandidate> findAllByUser_IdOrderByCreatedDate(Long userId);

    List<ChatToMemberCandidate> findAllByChat_IdOrderByCreatedDate(Long chatId);
}
