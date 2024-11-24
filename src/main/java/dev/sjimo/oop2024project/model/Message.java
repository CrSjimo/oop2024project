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

    private LocalDateTime createdDate;

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


}
