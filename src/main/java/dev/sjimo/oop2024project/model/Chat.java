package dev.sjimo.oop2024project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Chat {
    public enum Type {
        PRIVATE_CHAT,
        GROUP_CHAT,
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Optional<User> getUser1() {
        return Optional.ofNullable(user1);
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public Optional<User> getUser2() {
        return Optional.ofNullable(user2);
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}
