package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.BlockList;
import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("""
        SELECT c
        FROM Chat c
        WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)
    """)
    Optional<Chat> findPrivateChat(User user1, User user2);
}
