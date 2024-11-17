package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.UserData;

public class UserDataRequest {
    private String username;
    private UserData.Gender gender;
    private String gravatarEmail;
    private String description;

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
