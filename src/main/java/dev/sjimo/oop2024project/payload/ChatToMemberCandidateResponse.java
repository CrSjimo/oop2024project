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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatToMemberCandidate.Status getStatus() {
        return status;
    }

    public void setStatus(ChatToMemberCandidate.Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
