package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class BlockListResponse {
    private Long id;
    private Long user1_Id;
    private Long user2_Id;
    private LocalDateTime createdDate;

    public BlockListResponse(Long id, Long user1_Id, Long user2_Id, LocalDateTime createdDate){
        this.id = id;
        this.user1_Id = user1_Id;
        this.user2_Id = user2_Id;
        this.createdDate = createdDate;
    }
    public Long getId() {
        return id;
    }

    public Long getUser1() {
        return user1_Id;
    }

    public Long getUser2() {
        return user2_Id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
