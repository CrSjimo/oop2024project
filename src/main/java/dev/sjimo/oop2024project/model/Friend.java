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

    //FIXME 怎么只有一个备注名，两个人应该分别能给对方设置备注名
    private String commentName;

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

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getCommentName() {
        return commentName;
    }
}
