package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.*;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.service.ChatService;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/group")
    public ChatResponse createGroup(@RequestHeader("Authorization") String jwtToken, @RequestBody CreateGroupRequest createGroupRequest) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.createGroup(ownerId, createGroupRequest.getGroupName());
    }

    @GetMapping("/private_chat/{user1Id}/{user2Id}")
    public ChatResponse getPrivateChat(@RequestHeader("Authorization") String jwtToken, @PathVariable Long user1Id, @PathVariable Long user2Id) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        if (!user1Id.equals(ownerId)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return chatService.getPrivateChat(user1Id, user2Id);
    }

    @GetMapping("/group/{groupId}")
    public ChatResponse getGroupInfo(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long memberId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getGroupChat(memberId, groupId);
    }

    @PostMapping("/group/{groupId}")
    public void setGroupName(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @RequestBody SetGroupInfoRequest request) {
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.changeGroupName(ownerId, groupId, request.getName());
    }

    @DeleteMapping("/group/{groupId}")
    public void deleteOrQuitGroup(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long memberId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.deleteChat(memberId, groupId);
    }

    @GetMapping("/group/{groupId}/user")
    public List<ChatMemberResponse> getGroupUsers(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long memberId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getChatMember(memberId, groupId);
    }

    @DeleteMapping("/group/{groupId}/user/{userId}")
    public void removeMember(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @PathVariable Long userId) {
        Long adminId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.removeMember(adminId, userId, groupId);
    }

    @PostMapping("/group/{groupId}/user/{userId}/administrator")
    public void grantAdministrator(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @PathVariable Long userId) {
        Long owner = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.grantAdministrator(owner, userId, groupId);
    }

    @DeleteMapping("/group/{groupId}/user/{userId}/administrator")
    public void removeAdministrator(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @PathVariable Long userId) {
        Long owner = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.removeAdministrator(owner, userId, groupId);
    }

    @PostMapping("/group/{groupId}/user/{userId}/owner")
    public void changeGroupOwner(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @PathVariable Long userId) {
        Long owner = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.changeGroupOwner(owner, userId, groupId);
    }

    @PostMapping("/group/{groupId}/invite/{userId}")
    public void sendInvitation(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @PathVariable Long userId, @RequestBody GroupInvitationRequest groupInvitationRequest) {
        Long issuerId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.sendInvitation(issuerId, userId, groupId, groupInvitationRequest.getMessage());
    }

    @GetMapping("/group/{groupId}/invitation")
    public List<ChatToMemberCandidateResponse> getGroupInvitationsOfGroup(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getGroupChatInvitation(userId, groupId);
    }

    @GetMapping("/group_invitation")
    public List<ChatToMemberCandidateResponse> getGroupInvitationsOfUser(@RequestHeader("Authorization") String jwtToken) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getGroupChatApplication(userId);
    }

    @PostMapping("/group_invitation/{chatToMemberCandidateId}/accept")
    public void acceptChatInvitation(@RequestHeader("Authorization") String jwtToken, @PathVariable Long chatToMemberCandidateId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.acceptChatInvitation(userId, chatToMemberCandidateId);
    }

    @PostMapping("/group_invitation/{chatToMemberCandidateId}/reject")
    public void rejectChatInvitation(@RequestHeader("Authorization") String jwtToken, @PathVariable Long chatToMemberCandidateId) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.rejectChatInvitation(userId, chatToMemberCandidateId);
    }

    @PostMapping("/group/{groupId}/apply_for")
    public void applyToJoinGroup(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId, @RequestBody ApplyForGroupRequest applyForGroupRequest) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        chatService.applyToJoinGroup(userId, groupId, applyForGroupRequest.getMessage());
    }

    @GetMapping("/group_application")
    public List<MemberToChatCandidateResponse> getGroupApplications(@RequestHeader("Authorization") String jwtToken) {
        Long userId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getSelfGroupChatApplication(userId);
    }

    @GetMapping("/group/{groupId}/candidate")
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

    @GetMapping("/groups_of/{userId}")
    public List<ChatResponse> getAllGroupSelfIn(@RequestHeader("Authorization") String jwtToken, @PathVariable Long userId) {
        if (!userId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        return chatService.getAllGroup(userId);
    }

    @GetMapping("/chats_of/{userId}")
    public List<ChatResponse> getAllChats(@RequestHeader("Authorization") String jwtToken, @PathVariable Long userId) {
        if (!userId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        var list = new ArrayList<ChatResponse>();
        list.addAll(chatService.getAllPrivateChat(userId));
        list.addAll(chatService.getAllGroup(userId));
        return list;
    }

    @GetMapping("/find")
    public List<ChatResponse> findGroup(@RequestParam String q) {
        return chatService.findGroup(q);
    }
}
