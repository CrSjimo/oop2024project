package dev.sjimo.oop2024project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class FriendCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id",nullable = false)
    private User user1;         //发出申请的用户
    @ManyToOne
    @JoinColumn(name = "user2_id",nullable = false)
    private User user2;         //接到申请的用户

    @CreationTimestamp
    private LocalDateTime createdDate;

    private String message;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }

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

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
