package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.UserData;

public class UserDataResponse {

    private String username;
    private UserData.Gender gender;
    private String gravatarEmail;
    private String description;
    private String email;

    public UserDataResponse(String username, UserData.Gender gender, String gravatarEmail, String description, String email) {
        this.username = username;
        this.gender = gender;
        this.gravatarEmail = gravatarEmail;
        this.description = description;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public UserData.Gender getGender() {
        return gender;
    }

    public String getGravatarEmail() {
        return gravatarEmail;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }
}
