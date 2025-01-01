package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.UserData;

public class UserDataResponse {

    private final String username;
    private final UserData.Gender gender;
    private final String gravatarEmail;
    private final String description;
    private final String email;

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
