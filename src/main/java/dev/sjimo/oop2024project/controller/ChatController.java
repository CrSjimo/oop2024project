package dev.sjimo.oop2024project.controller;

import dev.sjimo.oop2024project.payload.*;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.service.ChatService;
import dev.sjimo.oop2024project.service.JwtService;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/private_chat/{user1Id}/{user2Id}")
    public ChatResponse getPrivateChat(@RequestHeader("Authorization") String jwtToken, @PathVariable Long user1Id, @PathVariable Long user2Id){
        Long ownerId = jwtService.extractUserId(jwtToken.replace("Bearer",""));
        if (!user1Id.equals(ownerId)){
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return chatService.getPrivateChat(user1Id,user1Id);
    }

    @GetMapping("/group/{groupId}")
    public ChatResponse getGroupInfo(@RequestHeader("Authorization") String jwtToken, @PathVariable Long groupId) {
        Long memberId = jwtService.extractUserId(jwtToken.replace("Bearer ", ""));
        return chatService.getGroupChat(memberId, groupId);
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
        return chatService.getAllChat(userId);
    }

    @GetMapping("/change_group_owner/{ownerId}/{newOwnerId}/{chatId}")
    public void changeGroupOwner(@RequestHeader("Authorization") String jwtToken, @PathVariable Long ownerId, @PathVariable Long newOwnerId, @PathVariable Long chatId) {
        if (!ownerId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        chatService.changeGroupOwner(ownerId,newOwnerId,chatId);
    }

    @GetMapping("/grantAdministrator/{ownerId}/{userId}/{chatId}")
    public void grantAdministrator(@RequestHeader("Authorization") String jwtToken, @PathVariable Long ownerId, @PathVariable Long userId, @PathVariable Long chatId) {
        if (!ownerId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        chatService.grantAdministrator(ownerId,userId,chatId);
    }

    @GetMapping("/removeAdministrator/{ownerId}/{administratorId}/{chatId}")
    public void removeAdministrator(@RequestHeader("Authorization") String jwtToken, @PathVariable Long ownerId, @PathVariable Long administratorId, @PathVariable Long chatId) {
        if (!ownerId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        chatService.removeAdministrator(ownerId,administratorId,chatId);
    }
    @GetMapping("/removeMember/{administratorId}/{userId}/{chatId}")
    public void removeMember(@RequestHeader("Authorization") String jwtToken, @PathVariable Long administratorId, @PathVariable Long userId, @PathVariable Long chatId) {
        if (!administratorId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        chatService.removeMember(administratorId,userId,chatId);
    }

    @GetMapping("/deleteChat/{ownerId}/{chatId}")
    public void deleteChat(@RequestHeader("Authorization") String jwtToken, @PathVariable Long ownerId, @PathVariable Long chatId) {
        if (!ownerId.equals(jwtService.extractUserId(jwtToken.replace("Bearer ", ""))))
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        chatService.deleteChat(ownerId,chatId);
    }
}
