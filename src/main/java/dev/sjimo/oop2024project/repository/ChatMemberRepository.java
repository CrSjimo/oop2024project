package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    Optional<ChatMember> findByUser_IdAndChat_Id (Long userId, Long chatId);
    boolean existsByUser_IdAndChat_Id(Long chatId, Long userId);

}
