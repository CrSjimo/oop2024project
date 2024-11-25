package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
