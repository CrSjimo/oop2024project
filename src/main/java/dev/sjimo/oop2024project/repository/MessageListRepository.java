package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageListRepository extends JpaRepository<Message,Long> {
    boolean existsByMessage_IdAndChat_IdAndSattus(Long message_Id, Long chat_Id);
    Optional<Message> findByMessage_IdAndChat_Id(Long message_Id, Long chat_Id);
}
