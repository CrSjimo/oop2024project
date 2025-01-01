package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.FriendCandidate;

import java.time.LocalDateTime;

public class FriendCandidateResponse {
    Long id;
    Long userId;
    String message;
    FriendCandidate.Status status;
    LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public FriendCandidate.Status getStatus() {
        return status;
    }

    public void setStatus(FriendCandidate.Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
