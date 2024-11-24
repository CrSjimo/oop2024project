package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

}
