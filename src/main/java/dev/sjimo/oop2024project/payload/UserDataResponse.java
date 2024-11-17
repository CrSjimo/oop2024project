package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.UserData;

public class UserDataResponse {

    private String username;
    private UserData.Gender gender;
    private String gravatarEmail;
    private String description;

    public UserDataResponse(String username, UserData.Gender gender, String gravatarEmail, String description) {
        this.username = username;
        this.gender = gender;
        this.gravatarEmail = gravatarEmail;
        this.description = description;
    }

}
