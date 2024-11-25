package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.model.User;

import java.time.LocalDateTime;

public class MessageRequest {
    Long id;
    User user;
    Chat chat;
    String message;
    LocalDateTime timestamp;
    Message.Status status;
    public MessageRequest(Long id, User user, Chat chat, String message,LocalDateTime timestamp, Message.Status status) {
        this.message = message;
        this.user = user;
        this.chat = chat;
        this.timestamp = timestamp;
        this.status = status;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
    public User getUser() {
        return user;
    }
    public Chat getChat() {
        return chat;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public Long getId() {
        return id;
    }
    public Message.Status getStatus() {
        return status;
    }
}
