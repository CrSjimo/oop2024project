package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.Chat;

import java.time.LocalDateTime;

public class ChatResponse {
    private final Long id;
    private final String name;
    private final Chat.Type type;
    private final LocalDateTime createdDate;
    private final Long user1Id;
    private final Long user2Id;

    public ChatResponse(Long id, String name, Chat.Type type, Long user1Id, Long user2Id, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Chat.Type getType() {
        return type;
    }

    public Long getUser1Id() {
        return user1Id;
    }

    public Long getUser2Id() {
        return user2Id;
    }
}
