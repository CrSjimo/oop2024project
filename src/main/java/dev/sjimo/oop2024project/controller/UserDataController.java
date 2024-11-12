package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.model.UserData;
import dev.sjimo.oop2024project.request.UserDataRequest;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.UserDataService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.apache.coyote.BadRequestException;
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
    public ResponseEntity<String> whoami(@RequestHeader("Authorization") String jwtToken) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return ResponseEntity.ok(userId.toString());
    }

    @GetMapping("/userdata/{id}")
    public ResponseEntity<UserDataRequest> getUserData(@PathVariable("id") Long userId) {
        UserDataRequest userDataRequest = userDataService.getUserData(userId);
        return ResponseEntity.ok(userDataRequest);
    }

    @PutMapping("/userdata/{id}")
    public ResponseEntity<String> updateUserData(@PathVariable("id") Long userId,@RequestHeader("Authorization") String jwtToken, @RequestBody UserDataRequest userDataRequest) {
        Long nowUserId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!nowUserId.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        userDataService.setUserData(userId, userDataRequest);
        return ResponseEntity.ok().build();
    }

}
