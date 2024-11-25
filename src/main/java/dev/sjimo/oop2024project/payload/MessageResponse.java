package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.model.User;

import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private String message;
    private User user;
    private Chat chat;
    private LocalDateTime createdDate;
    private Message.Status status;

    public MessageResponse(Long id, String message, User user, Chat chat, LocalDateTime createdDate, Message.Status status) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.chat = chat;
        this.createdDate = createdDate;
        this.status = status;
    }
    public Long getId() {
        return id;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Message.Status getStatus() {
        return status;
    }
}
