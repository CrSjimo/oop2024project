package dev.sjimo.oop2024project.payload;

public class ResetEmailRequest {
    private String token;
    private String email;
    public ResetEmailRequest(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getNewEmail() {
        return email;
    }
}
