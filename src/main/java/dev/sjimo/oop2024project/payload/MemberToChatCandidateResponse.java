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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setStatus(MemberToChatCandidate.Status status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
