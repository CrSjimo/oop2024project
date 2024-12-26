package dev.sjimo.oop2024project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "chat_id",nullable = false)
    private Chat chat;
    private String message;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
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
