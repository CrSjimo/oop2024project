package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.ChatToMemberCandidate;
import org.apache.el.stream.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatToMemberCandidateRepository extends JpaRepository<ChatToMemberCandidate, Long> {
    boolean existsByUser_IdAndChat_IdAndStatus(Long userId, Long chatId, ChatToMemberCandidate.Status status);

}
