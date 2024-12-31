package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.model.User;

import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private String message;
    private Long userId;
    private Long chatId;
    private LocalDateTime createdDate;
    private Message.Status status;

    public MessageResponse(Long id, String message, User user, Chat chat, LocalDateTime createdDate, Message.Status status) {
        this.id = id;
        this.message = message;
        this.userId = user.getId();
        this.chatId = chat.getId();
        this.createdDate = createdDate;
        this.status = status;
    }
    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getChatId() {
        return chatId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Message.Status getStatus() {
        return status;
    }
}
