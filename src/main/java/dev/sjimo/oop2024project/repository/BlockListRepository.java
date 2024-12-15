package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.BlockList;
import dev.sjimo.oop2024project.model.FriendCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockListRepository extends JpaRepository<BlockList, Long> {
    boolean existsByUser1_IdAndUser2_Id(Long user1_Id, Long user2_Id);
    Optional<BlockList> findByUser1_IdAndUser2_Id(Long user1_Id,Long user2_Id);
    List<BlockList> findAllByUser1_IdOrderByCreatedDate(Long user1_Id);
}
