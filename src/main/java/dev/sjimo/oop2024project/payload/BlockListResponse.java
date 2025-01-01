package dev.sjimo.oop2024project.payload;

import java.time.LocalDateTime;

public class BlockListResponse {
    private final Long id;
    private final Long userId;
    private final LocalDateTime createdDate;

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
