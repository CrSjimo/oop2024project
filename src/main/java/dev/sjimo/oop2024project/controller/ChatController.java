package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.*;
import dev.sjimo.oop2024project.service.ChatService;
import dev.sjimo.oop2024project.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/group")
    public ChatResponse createGroup(@RequestHeader("Authorization") String jwtToken, @RequestBody CreateGroupRequest createGroupRequest) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.createGroup(ownerId, createGroupRequest.getGroupName());
    }

    @GetMapping("/group/{groupId}")
    public ChatResponse getGroupInfo(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        //TODO 获取群信息
        return null;
    }

    @GetMapping("/group/{groupId}/users")
    public Object/* TODO */ getGroupUsers(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        //TODO 获取群成员列表
        return null;
    }



    @PostMapping("/group/{groupId}/invite/{userId}")
    public void sendInvitation(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @PathVariable Long userId, @RequestBody GroupInvitationRequest groupInvitationRequest) {
        Long issuerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.sendInvitation(issuerId, userId, groupId, groupInvitationRequest.getMessage());
    }

    @GetMapping("/group/{groupId}/invitations")
    public List<ChatToMemberCandidateResponse> getGroupInvitationsOfGroup(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getGroupChatInvitation(userId, groupId);
    }

    @GetMapping("/group_invitations")
    public List<ChatToMemberCandidateResponse> getGroupInvitationsOfUser(@RequestHeader("Authorization") String jwtToken) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getGroupChatApplication(userId);
    }

    @GetMapping("/group_invitation/{chatToMemberCandidateId}/accept")
    public void acceptChatInvitation(@RequestHeader("Authorization") String jwtToken, @PathVariable Long chatToMemberCandidateId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.acceptChatInvitation(userId, chatToMemberCandidateId);
    }

    @GetMapping("/group_invitation/{chatToMemberCandidateId}/reject")
    public void rejectChatInvitation(@RequestHeader("Authorization") String jwtToken, @PathVariable Long chatToMemberCandidateId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.rejectChatInvitation(userId, chatToMemberCandidateId);
    }



    @PostMapping("/group/{groupId}/apply_for")
    public void applyToJoinGroup(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @RequestBody ApplyForGroupRequest applyForGroupRequest) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.applyToJoinGroup(userId, groupId, applyForGroupRequest.getMessage());
    }

    @GetMapping("/group_applications")
    public List<MemberToChatCandidateResponse> getGroupApplications(@RequestHeader("Authorization") String jwtToken) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getSelfGroupChatApplication(userId);
    }

    @GetMapping("/group/{groupId}/candidates")
    public List<MemberToChatCandidateResponse> getGroupCandidatesOfGroup(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getUserApplication(userId, groupId);
    }

    @PostMapping("/group_candidate/{memberToChatCandidateId}/accept")
    public void acceptChatApplication(@RequestHeader("Authorization") String jwtToken, @PathVariable Long memberToChatCandidateId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.acceptChatApplication(userId, memberToChatCandidateId);
    }

    @PostMapping("/group_candidate/{memberToChatCandidateId}/reject")
    public void rejectChatApplication(@RequestHeader("Authorization") String jwtToken, @PathVariable Long memberToChatCandidateId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.rejectChatApplication(userId, memberToChatCandidateId);
    }


}
