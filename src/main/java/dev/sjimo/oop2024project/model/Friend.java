package dev.sjimo.oop2024project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id",nullable = false)
    private User user1;
    @ManyToOne
    @JoinColumn(name = "user2_id",nullable = false)
    private User user2;
    private LocalDateTime createdDate;

    private String commentName1;//user2给user1的备注名
    private String commentName2;//user1给user2的备注名

    @OneToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public Long getId() {
        return id;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getUser2() {
        return user2;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCommentName1(String commentName1) {
        this.commentName1 = this.commentName1;
    }

    public void setCommentName2(String commentName2) {
        this.commentName2 = this.commentName2;
    }

    public String getCommentName1() {
        return commentName1;
    }
    public String getCommentName2() { return commentName2; }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
