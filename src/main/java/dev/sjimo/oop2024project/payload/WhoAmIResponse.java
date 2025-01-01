package dev.sjimo.oop2024project.payload;

public class WhoAmIResponse {
    private final Long id;

    public WhoAmIResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
