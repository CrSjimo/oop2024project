package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public Message sendMessage(@RequestParam Long userId,@RequestParam Long chatId, @RequestParam String content) {
        return messageService.sendMessage(userId,chatId,content);
    }
    @DeleteMapping("/{messageId}")
    public void revokeMessage(@RequestParam Long userId, @RequestParam Long chatId, @PathVariable Long messageId) {
        messageService.revokeMessage(userId, chatId, messageId);
    }

    @GetMapping("/{messageId}")
    public Message getMessage(@RequestParam Long userId, @RequestParam Long chatId, @PathVariable Long messageId) {
        return messageService.getMessage(userId, chatId, messageId);
    }
    // announcement
    @PostMapping("/announcements")
    public Message sendAnnouncement(@RequestParam Long userId, @RequestParam Long chatId, @RequestParam String content) {
        return messageService.sendAnnouncement(userId, chatId, content);
    }
    @DeleteMapping("/announcements/{announcementId}")
    public void revokeAnnouncement(@RequestParam Long userId,@RequestParam Long chatId, @PathVariable Long announcementId) {
        messageService.revokeGroupAnnouncement(userId,chatId,announcementId);
    }
}
