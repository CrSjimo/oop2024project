package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.FriendCandidate;
import dev.sjimo.oop2024project.model.MemberToChatCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberToChatCandidateRepository extends JpaRepository<MemberToChatCandidate, Long> {
    Optional<MemberToChatCandidate> findByUser_IdAndChat_Id(Long userId, Long chatId);
    List<MemberToChatCandidate> findAllByUser_IdOrderByCreatedDate(Long userId);
    List<MemberToChatCandidate> findAllByChat_IdOrderByCreatedDate(Long chatId);
}
