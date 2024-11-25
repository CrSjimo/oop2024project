package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.ChatToMemberCandidate;

import java.time.LocalDateTime;

public class ChatToMemberCandidateResponse {
    Long id;
    Long userId;
    Long issuerId;
    Long chatId;
    String message;
    ChatToMemberCandidate.Status status;
    LocalDateTime createdDate;

    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setStatus(ChatToMemberCandidate.Status status) {
        this.status = status;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
