package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.ChatMember;

import java.time.LocalDateTime;

public class ChatMemberResponse {
    private final Long chatId;
    private final String chatName;
    private final ChatMember.MemberType memberType;
    private final LocalDateTime createdDate;
    private final Long userId;
    private final String userName;

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
