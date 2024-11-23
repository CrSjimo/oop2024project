package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.FriendCandidateResponse;
import dev.sjimo.oop2024project.payload.FriendApplicationRequest;
import dev.sjimo.oop2024project.payload.FriendResponse;
import dev.sjimo.oop2024project.service.ContactService;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private JwtService jwtService;

    /**
     * 更新好友信息，如备注名等
     * @param jwtToken
     * @param user1Id
     * @param user2Id
     */
    @PostMapping("/{user1Id}/friend/{user2Id}")
    public void updateFriend(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }

        //TODO 更新备注名等信息

    }

    /**
     * 删除好友
     * @param jwtToken
     * @param user1Id
     * @param user2Id
     */
    @DeleteMapping("/{user1Id}/friend/{user2Id}")
    public void deleteFriend(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.deleteFriend(user1Id, user2Id);
    }

    /**
     * 列出所有好友
     * @param jwtToken
     * @param user1Id
     * @return
     */
    @GetMapping("/{user1Id}/friend")
    public List<FriendResponse> listFriends(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return contactService.listFriends(user1Id);
    }

    /**
     * 增加用户到黑名单
     * @param jwtToken
     * @param user1Id
     * @param user2Id
     */
    @PostMapping("/{user1Id}/blocklist/{user2Id}")
    public void addToBlockList(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.blockOther(user1Id, user2Id);
    }

    /**
     * 从黑名单移除用户
     * @param jwtToken
     * @param user1Id
     * @param user2Id
     */
    @DeleteMapping("/{user1Id}/blocklist/{user2Id}")
    public void deleteFromBlockList(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.deleteBlock(user1Id, user2Id);
    }

    /**
     * 列出所有收到的好友申请
     * @param jwtToken
     * @param user1Id
     * @return
     */
    @GetMapping("/{user1Id}/friend_candidate/")
    public List<FriendCandidateResponse> getFriendCandidates(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return contactService.getOtherFriendCandidates(user1Id);
    }

    /**
     * 接受指定的好友申请
     * @param jwtToken
     * @param user1Id
     * @param id
     */
    @PostMapping("/{user1Id}/friend_candidate/{id}/accept")
    public void acceptFriendCandidate(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("id") Long id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.acceptFriendApplication(user1Id, id);
    }

    /**
     * 拒绝指定的好友申请
     * @param jwtToken
     * @param user1Id
     * @param id
     */
    @PostMapping("/{user1Id}/friend_candidate/{id}/reject")
    public void rejectFriendCandidate(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("id") Long id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.rejectFriendApplication(user1Id, id);
    }

    /**
     * 列出所有发给别人的好友申请
     * @param jwtToken
     * @param user1Id
     * @return
     */
    @GetMapping("/{user1Id}/friend_application/")
    public List<FriendCandidateResponse> getFriendApplications(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return contactService.getSelfFriendCandidates(user1Id);
    }

    /**
     * 发送好友申请，也就是加好友
     * @param jwtToken
     * @param user1Id
     * @param user2Id
     * @param friendRequest
     */
    @PostMapping("/{user1Id}/friend_application/{user2Id}")
    public void addFriend(@RequestHeader("Authorization") String jwtToken, @PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id, @RequestBody FriendApplicationRequest friendRequest) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        contactService.addFriend(user1Id, user2Id, friendRequest.getMessage());
    }

}
