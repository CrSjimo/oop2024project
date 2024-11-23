package dev.sjimo.oop2024project.payload;

public class LoginResponse {
    private String token;
    public LoginResponse(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
}
