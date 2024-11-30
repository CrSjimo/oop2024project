package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.Chat;

public class ChatResponse {
    private Long id;
    private String name;
    private Chat.Type type;
    private Long user1Id;
    private Long user2Id;

    public ChatResponse(Long id, String name, Chat.Type type, Long user1Id, Long user2Id) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
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