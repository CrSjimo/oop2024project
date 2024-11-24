package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository {
    @Query("""
        SELECT c
        FROM Chat c
        JOIN Friend f ON f.chat = c
        WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)
    """)
    Optional<Chat> findPrivateChat(User user1, User user2);
}
