package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.MemberToChatCandidate;

import java.time.LocalDateTime;

public class MemberToChatCandidateResponse {
    Long id;
    Long userId;
    Long chatId;
    String message;
    MemberToChatCandidate.Status status;
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public MemberToChatCandidate.Status getStatus() {
        return status;
    }

    public void setStatus(MemberToChatCandidate.Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
