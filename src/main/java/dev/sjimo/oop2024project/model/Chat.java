package dev.sjimo.oop2024project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
}
