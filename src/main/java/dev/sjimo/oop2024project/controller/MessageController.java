package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.payload.FriendApplicationRequest;
import dev.sjimo.oop2024project.payload.MessageRequest;
import dev.sjimo.oop2024project.payload.MessageResponse;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.MessageService;
import dev.sjimo.oop2024project.service.PushService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    PushService pushService = new PushService();

    @PostMapping( "/send/{userId}/{chatId}")
    public void sendMessage(@RequestHeader("Authorization") String jwtToken, @PathVariable Long userId, @PathVariable Long chatId, @RequestBody MessageRequest messageRequest) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageService.sendMessage(userId,chatId,messageRequest);
//        pushService.pushTo(userId,"Send success");
    }

    @DeleteMapping("/delete_message/{userId}/{chatId}/{messageId}")
    public void revokeMessage(@RequestHeader("Authorization") String jwtToken,@PathVariable Long userId, @PathVariable Long chatId, @PathVariable Long messageId) {
        Long userId1 = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId1.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageService.revokeMessage(userId, messageId);
//        pushService.pushTo(userId,"Revoke success");
    }

    @GetMapping("/get_messages/{userId}/{chatId}")
    public List<MessageResponse> getMessages(@RequestHeader("Authorization") String jwtToken, @PathVariable Long userId, @PathVariable Long chatId) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return messageService.getMessage(userId, chatId);
    }

    @GetMapping("/get_message_in_limit/{userId}/{messageId}/{limit}")
    public List<MessageResponse> getMessagesInLimit(@RequestHeader("Authorization") String jwtToken, @PathVariable Long userId, @PathVariable Long messageId, @PathVariable Integer limit) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return messageService.getMessages(userId, messageId, limit);
    }

    @PostMapping("/get_latest_message/{userId}/{chatId}")
    public MessageResponse getLatestMessage(@RequestHeader("Authorization") String jwtToken, @PathVariable Long userId, @PathVariable Long chatId) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return messageService.getLatestMessage(userId, chatId);
    }

    // announcement
    @PostMapping("/announcements/{userId}/{chatId}")
    public Message sendAnnouncement(@RequestHeader("Authorization") String jwtToken,@PathVariable Long userId, @PathVariable Long chatId, @RequestBody MessageRequest messageRequest) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
//        pushService.pushTo(userId,"Announce success");
        return messageService.sendAnnouncement(userId, chatId, messageRequest.getMessage());
    }
    @PostMapping("/get_announcements/{userId}/{chatId}")
    public List<MessageResponse> getAnnounces(@RequestHeader("Authorization") String jwtToken,@PathVariable Long userId, @PathVariable Long chatId) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!userId.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return messageService.getMessage(userId, chatId);
    }
    @DeleteMapping("/delete_announcements/{announcementId}")
    public void revokeAnnouncement(@RequestHeader("Authorization") String jwtToken,@RequestParam Long user1Id,@RequestParam Long chatId, @PathVariable Long announcementId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageService.revokeGroupAnnouncement(userId,chatId,announcementId);
//        pushService.pushTo(userId,"Revoke success");
    }
}
