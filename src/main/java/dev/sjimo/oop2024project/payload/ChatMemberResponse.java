package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.ChatMember;

import java.time.LocalDateTime;

public class ChatMemberResponse {
    private Long chatId;
    private String chatName;
    private ChatMember.MemberType memberType;
    private LocalDateTime createdDate;
    private Long userId;
    private String userName;
    public ChatMemberResponse(Long chatId, String chatName, ChatMember.MemberType memberType, LocalDateTime createdDate, Long userId, String userName) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.memberType = memberType;
        this.createdDate = createdDate;
        this.userId = userId;
        this.userName = userName;
    }
    public Long getChatId() {
        return chatId;
    }
    public String getChatName() {
        return chatName;
    }

    public ChatMember.MemberType getMemberType() {
        return memberType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public Long getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
}
