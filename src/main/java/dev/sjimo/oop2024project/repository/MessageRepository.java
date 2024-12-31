package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("SELECT m FROM Message m " +
            "WHERE m.chat.id = :chatId AND m.createdDate = (SELECT MAX(m2.createdDate) FROM Message m2 WHERE m2.chat.id = :chatId)")
    Optional<Message> findLatestMessageIdByChatId(@Param("chatId") Long chatId);

    @Query("""
    SELECT m
    FROM Message m
    WHERE m.chat.id = :chatId
      AND m.createdDate < (
        SELECT m2.createdDate
        FROM Message m2
        WHERE m2.id = :messageId
      )
    ORDER BY m.createdDate DESC
""")
    List<Message> findMessagesBefore(
            @Param("chatId") Long chatId,
            @Param("messageId") Long messageId,
            Pageable pageable
    );

    @Query("""
    SELECT m
    FROM Message m
    WHERE m.chat.id = :chatId
      AND m.createdDate > (
        SELECT m2.createdDate
        FROM Message m2
        WHERE m2.id = :messageId
      )
    ORDER BY m.createdDate DESC
""")
    List<Message> findMessagesAfter(
            @Param("chatId") Long chatId,
            @Param("messageId") Long messageId
    );

    List<Message> findAllByUser_IdAndChat_Id(Long user_Id, Long chat_Id);
}
