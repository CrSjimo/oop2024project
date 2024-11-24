package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.FriendCandidate;

import java.time.LocalDateTime;

public class MemberToChatCandidateResponse {
    Long id;
    Long chatId;
    String message;
    FriendCandidate.Status status;
    LocalDateTime createdDate;

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(Long chatId) {
        this.chatId = chatId;
    }

    public void setStatus(FriendCandidate.Status status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
