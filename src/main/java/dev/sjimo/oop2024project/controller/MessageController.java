package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.payload.MessageResponse;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.MessageService;
import dev.sjimo.oop2024project.service.PushService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    PushService pushService = new PushService();

    @PostMapping("/send")
    public void sendMessage(@RequestHeader("Authorization") String jwtToken,@RequestParam Long user1Id,@RequestParam Long chatId, @RequestParam String content) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageService.sendMessage(userId,chatId,content);
        pushService.pushTo(userId,"Send success");
    }
    @DeleteMapping("/deleteMessage/{messageId}")
    public void revokeMessage(@RequestHeader("Authorization") String jwtToken,@RequestParam Long user1Id, @RequestParam Long chatId, @PathVariable Long messageId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageService.revokeMessage(userId, messageId);
        pushService.pushTo(userId,"Revoke success");
    }

    @PostMapping("/getMessage/{messageId}")
    public MessageResponse getMessage(@RequestHeader("Authorization") String jwtToken,@RequestParam Long user1Id, @RequestParam Long chatId, @PathVariable Long messageId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return messageService.getMessage(userId, chatId, messageId);
    }
    // announcement
    @PostMapping("/announcements")
    public Message sendAnnouncement(@RequestHeader("Authorization") String jwtToken,@RequestParam Long user1Id, @RequestParam Long chatId, @RequestParam String content) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return messageService.sendAnnouncement(userId, chatId, content);
    }
    @DeleteMapping("/announcements/{announcementId}")
    public void revokeAnnouncement(@RequestHeader("Authorization") String jwtToken,@RequestParam Long user1Id,@RequestParam Long chatId, @PathVariable Long announcementId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(userId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageService.revokeGroupAnnouncement(userId,chatId,announcementId);
    }
}
