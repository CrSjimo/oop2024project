package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.FriendCandidate;

import java.time.LocalDateTime;

public class FriendCandidateResponse {
    Long userId;
    String message;
    FriendCandidate.Status status;
    LocalDateTime createdDate;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStatus(FriendCandidate.Status status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}
