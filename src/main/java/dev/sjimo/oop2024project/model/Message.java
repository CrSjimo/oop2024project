package dev.sjimo.oop2024project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "chat_id",nullable = false)
    private Chat chat;
    private String message;
    private LocalDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public enum Status {
        UNREAD,
        SENDING,
        READ,
    }



    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setChat(Chat chat){this.chat = chat;}

    public Chat getChat(){return this.chat;}
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
