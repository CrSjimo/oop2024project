package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.UserDataRequest;
import dev.sjimo.oop2024project.payload.UserDataResponse;
import dev.sjimo.oop2024project.payload.WhoAmIResponse;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.UserDataService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserDataController {
    @Autowired
    private UserDataService userDataService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/whoami")
    public WhoAmIResponse whoami(@RequestHeader("Authorization") String jwtToken) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return new WhoAmIResponse(userId);
    }

    @GetMapping("/userdata/{id}")
    public UserDataResponse getUserData(@PathVariable("id") Long userId) {
        return userDataService.getUserData(userId);
    }

    @PutMapping("/userdata/{id}")
    public void updateUserData(@PathVariable("id") Long userId,@RequestHeader("Authorization") String jwtToken, @RequestBody UserDataRequest userDataRequest) {
        Long nowUserId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!nowUserId.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        userDataService.setUserData(userId, userDataRequest);
    }

}
