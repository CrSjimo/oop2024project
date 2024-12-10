package dev.sjimo.oop2024project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChatMember {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public enum MemberType {
        GROUP_OWNER,
        ADMINISTRATOR,
        REGULAR_MEMBER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private MemberType memberType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
