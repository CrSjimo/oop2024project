package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Friend;
import dev.sjimo.oop2024project.model.FriendCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendCandidateRepository extends JpaRepository<FriendCandidate, Long> {
    boolean existsByUser1_IdAndUser2_IdAndStatus(Long user1_Id, Long user2_Id, FriendCandidate.Status status);

    Optional<FriendCandidate> findByUser1_IdAndUser2_IdAndStatus(Long user1_Id, Long user2_Id, FriendCandidate.Status status);
    List<FriendCandidate> findAllByUser1_IdOrderByCreatedDate(Long user1_Id);
    List<FriendCandidate> findAllByUser2_IdOrderByCreatedDate(Long user2_Id);
}
