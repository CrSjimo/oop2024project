package dev.sjimo.oop2024project.repository;

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

    List<Chat> findByUser1OrUser2(User user1, User user2);

    @Query("""
                SELECT c
                FROM Chat c
                WHERE
                    c.type = 1 AND (c.name LIKE CONCAT('%', :searchString, '%') OR c.id = CAST(:searchString AS LONG))
                ORDER BY
                    CASE
                        WHEN c.id = CAST(:searchString AS LONG) THEN 1
                        WHEN c.name LIKE CONCAT('%', :searchString, '%') THEN LENGTH(c.name)
                        ELSE 1
                    END
                    ASC
            """)
    List<Chat> findByChatLike(String searchString);
}
