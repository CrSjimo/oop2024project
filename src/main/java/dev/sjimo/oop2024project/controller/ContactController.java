package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.FriendCandidatesResponse;
import dev.sjimo.oop2024project.payload.FriendRequest;
import dev.sjimo.oop2024project.service.ContactService;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/{user1Id}/friend/{user2Id}")
    public ResponseEntity<String> addFriend(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id, FriendRequest friendRequest) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.addFriend(user1Id, user2Id, friendRequest.getMessage());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{user1Id}/friend/{user2Id}")
    public ResponseEntity<String> deleteFriend(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.deleteFriend(user1Id, user2Id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{user1Id}/blocklist/{user2Id}")
    public ResponseEntity<String> addToBlockList(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.blockOther(user1Id, user2Id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{user1Id}/blocklist/{user2Id}")
    public ResponseEntity<String> deleteFromBlockList(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.deleteBlock(user1Id, user2Id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{user1Id}/friend_candidates/")
    public ResponseEntity<FriendCandidatesResponse> getFriendCandidates(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return ResponseEntity.ok(contactService.getOtherFriendCandidates(user1Id));
    }

    @PostMapping("/{user1Id}/friend_candidates/{id}/accept")
    public ResponseEntity<String> acceptFriendCandidate(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("id") Long id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.acceptFriendApplication(user1Id, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{user1Id}/friend_candidates/{id}/reject")
    public ResponseEntity<String> rejectFriendCandidate(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("id") Long id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.rejectFriendApplication(user1Id, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{user1Id}/friend_applications/")
    public ResponseEntity<FriendCandidatesResponse> getFriendApplications(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return ResponseEntity.ok(contactService.getSelfFriendCandidates(user1Id));
    }

}
