package dev.sjimo.oop2024project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MemberToChatCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;          //申请加群用户

    @ManyToOne
    @JoinColumn(name = "chat_id",nullable = false)
    private Chat chat;

    private String message;
    private LocalDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Chat getChat() {
        return chat;
    }
    public void setChat(Chat chat) {
        this.chat = chat;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}