package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.MessageRequest;
import dev.sjimo.oop2024project.payload.MessageResponse;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.service.MessageService;
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

    @PutMapping("/chat/{chatId}")
    public void sendMessage(@RequestHeader("Authorization") String jwtToken, @PathVariable Long chatId, @RequestBody MessageRequest messageRequest) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        messageService.sendMessage(userId, chatId, messageRequest);
//        pushService.pushTo(userId,"Send success");
    }

    @DeleteMapping("/{messageId}")
    public void revokeMessage(@RequestHeader("Authorization") String jwtToken, @PathVariable Long messageId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        messageService.revokeMessage(userId, messageId);
//        pushService.pushTo(userId,"Revoke success");
    }

    @GetMapping("/list_before")
    public List<MessageResponse> getMessagesInLimit(@RequestHeader("Authorization") String jwtToken, @RequestParam Long messageId, @RequestParam Integer limit) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return messageService.getMessages(userId, messageId, limit);
    }

    @GetMapping("/list_after")
    public List<MessageResponse> getMessagesAfter(@RequestHeader("Authorization") String jwtToken, @RequestParam Long messageId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return messageService.getMessagesAfter(userId, messageId);
    }

    @GetMapping("/chat/{chatId}/latest")
    public MessageResponse getLatestMessage(@RequestHeader("Authorization") String jwtToken, @PathVariable Long chatId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return messageService.getLatestMessage(userId, chatId);
    }
}
