package dev.sjimo.oop2024project.request;

import dev.sjimo.oop2024project.model.UserData;

public class UserDataRequest {
    private String username;
    private UserData.Gender gender;
    private String gravatarEmail;
    private String description;

    public UserDataRequest(String username, UserData.Gender gender, String gravatarEmail, String description) {
        this.username = username;
        this.gender = gender;
        this.gravatarEmail = gravatarEmail;
        this.description = description;
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
}
