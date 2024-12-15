package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.payload.MessageResponse;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.MessageService;
import dev.sjimo.oop2024project.service.PushService;
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
    public void sendMessage(@RequestHeader("Authorization") String jwtToken,@RequestParam Long userId,@RequestParam Long chatId, @RequestParam String content) {
        Long userId1 = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (userId1 == userId) {
            messageService.sendMessage(userId,chatId,content);
            pushService.pushTo(userId,"Send success");

        }

    }
    @DeleteMapping("/deleteMessage/{messageId}")
    public void revokeMessage(@RequestHeader("Authorization") String jwtToken,@RequestParam Long userId, @RequestParam Long chatId, @PathVariable Long messageId) {
        Long userId1 = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (userId1 == userId) {
            messageService.revokeMessage(userId, messageId);
            pushService.pushTo(userId,"Revoke success");
        }
    }

    @PostMapping("/getMessage/{messageId}")
    public MessageResponse getMessage(@RequestHeader("Authorization") String jwtToken,@RequestParam Long userId, @RequestParam Long chatId, @PathVariable Long messageId) {
        return messageService.getMessage(userId, chatId, messageId);
    }
    // announcement
    @PostMapping("/announcements")
    public Message sendAnnouncement(@RequestHeader("Authorization") String jwtToken,@RequestParam Long userId, @RequestParam Long chatId, @RequestParam String content) {
        return messageService.sendAnnouncement(userId, chatId, content);
    }
    @DeleteMapping("/announcements/{announcementId}")
    public void revokeAnnouncement(@RequestHeader("Authorization") String jwtToken,@RequestParam Long userId,@RequestParam Long chatId, @PathVariable Long announcementId) {
        Long userId1 = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (userId1 == userId) {
            messageService.revokeGroupAnnouncement(userId,chatId,announcementId);
        }

    }
}
