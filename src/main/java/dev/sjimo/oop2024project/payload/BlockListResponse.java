package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class BlockListResponse {
    private Long id;
    private Long userId;
    private LocalDateTime createdDate;

    public BlockListResponse(Long id, Long userId, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.createdDate = createdDate;
    }
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
