package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.*;
import dev.sjimo.oop2024project.payload.ChatMemberResponse;
import dev.sjimo.oop2024project.payload.ChatResponse;
import dev.sjimo.oop2024project.payload.ChatToMemberCandidateResponse;
import dev.sjimo.oop2024project.payload.MemberToChatCandidateResponse;
import dev.sjimo.oop2024project.repository.*;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    ChatMemberRepository chatMemberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    ChatToMemberCandidateRepository chatToMemberCandidateRepository;
    @Autowired
    private MemberToChatCandidateRepository memberToChatCandidateRepository;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private UserDataRepository userDataRepository;

    public ChatResponse getPrivateChat(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user1.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        User user2 = userRepository.findById(user2Id).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user2.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        if (!friendRepository.existsByUser1AndUser2(user1, user2))
            throw new ResponseException(ErrorCode.NOT_BE_FRIEND);
        var chat = chatRepository.findPrivateChat(user1, user2).orElseGet(() -> {
            var c = new Chat();
            c.setType(Chat.Type.PRIVATE_CHAT);
            c.setUser1(user1);
            c.setUser2(user2);
            return chatRepository.save(c);
        });
        return new ChatResponse(chat.getId(), chat.getName(), chat.getType(), chat.getUser1().map(User::getId).orElse(null), chat.getUser2().map(User::getId).orElse(null),chat.getCreatedDate());
    }

    public ChatResponse createGroup(Long ownerId, String groupName) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!owner.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        var chat = new Chat();
        chat.setName(groupName);
        chat.setType(Chat.Type.GROUP_CHAT);
        chatRepository.save(chat);
        var chatMember = new ChatMember();
        chatMember.setUser(owner);
        chatMember.setChat(chat);
        chatMember.setMemberType(ChatMember.MemberType.GROUP_OWNER);
        chatMemberRepository.save(chatMember);
        return new ChatResponse(chat.getId(), chat.getName(), chat.getType(), null, null,chat.getCreatedDate());
    }

    //用户加入群聊
    private void joinGroupChat(User user, Chat chat) {
        ChatMember chatMemberEntity = new ChatMember();
        chatMemberEntity.setChat(chat);
        chatMemberEntity.setUser(user);
        chatMemberEntity.setMemberType(ChatMember.MemberType.REGULAR_MEMBER);
        chatMemberRepository.save(chatMemberEntity);
    }

    //群向用户发送邀请
    public void sendInvitation(Long issuerId, Long userId, Long chatId, String message) {
        User sender = userRepository.findById(issuerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));//判读发送者是否存在
        if (!sender.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(issuerId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));

        if (chat.getType() != Chat.Type.GROUP_CHAT)
            throw new ResponseException(ErrorCode.CHAT_NOT_GROUP);

        if (chatToMemberCandidateRepository.existsByUser_IdAndChat_IdAndStatus(userId, chatId, ChatToMemberCandidate.Status.PENDING)) {
            throw new ResponseException(ErrorCode.INVITATION_ALREADY_EXIST);
        }

        if (chatMemberRepository.existsByUser_IdAndChat_Id(chatId, userId)) {
            throw new ResponseException(ErrorCode.USER_ALREADY_EXIST);
        }

        var memberToChatCandidate = memberToChatCandidateRepository.findByUser_IdAndChat_Id(userId, chatId);
        if (memberToChatCandidate.isPresent()) {
            joinGroupChat(user, chat);
            return;
        }
        ChatToMemberCandidate chatToMemberCandidateEntity = new ChatToMemberCandidate();
        chatToMemberCandidateEntity.setChat(chat);
        chatToMemberCandidateEntity.setUser(user);
        chatToMemberCandidateEntity.setIssuer(sender);
        chatToMemberCandidateEntity.setMessage(message);
        chatToMemberCandidateEntity.setStatus(ChatToMemberCandidate.Status.PENDING);
        chatToMemberCandidateRepository.save(chatToMemberCandidateEntity);
    }

    //申请加入群聊
    public void applyToJoinGroup(Long userId, Long chatId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        if (chat.getType() != Chat.Type.GROUP_CHAT)
            throw new ResponseException(ErrorCode.CHAT_NOT_GROUP);

        if (chatToMemberCandidateRepository.existsByUser_IdAndChat_IdAndStatus(userId, chatId, ChatToMemberCandidate.Status.PENDING)) {
            throw new ResponseException(ErrorCode.INVITATION_ALREADY_EXIST);
        }

        if (chatMemberRepository.existsByUser_IdAndChat_Id(chatId, userId)) {
            throw new ResponseException(ErrorCode.USER_ALREADY_EXIST);
        }

        var chatToMemberCandidate = chatToMemberCandidateRepository.findByUser_IdAndChat_Id(userId, chatId);
        if (chatToMemberCandidate.isPresent()) {
            joinGroupChat(user, chat);
            return;
        }
        MemberToChatCandidate memberToChatCandidateEntity = new MemberToChatCandidate();
        memberToChatCandidateEntity.setChat(chat);
        memberToChatCandidateEntity.setUser(user);
        memberToChatCandidateEntity.setStatus(MemberToChatCandidate.Status.PENDING);
        memberToChatCandidateRepository.save(memberToChatCandidateEntity);
    }

    /**
     * 用户接受群的邀请
     *
     * @param userId 传入的应该是用户的id
     * @return
     */
    public void acceptChatInvitation(Long userId, Long chatToMemberCandidateId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatToMemberCandidate chatToMemberCandidate = chatToMemberCandidateRepository.findById(chatToMemberCandidateId).orElseThrow(() -> new ResponseException(ErrorCode.INVITATION_NOT_EXIST));
        if (chatToMemberCandidate.getUser() != user)
            throw new ResponseException(ErrorCode.INVITATION_NOT_EXIST);
        if (chatToMemberCandidate.getStatus() != ChatToMemberCandidate.Status.PENDING)
            throw new ResponseException(ErrorCode.INVITATION_SOLVED);
        chatToMemberCandidate.setStatus(ChatToMemberCandidate.Status.ACCEPTED);
        chatToMemberCandidateRepository.save(chatToMemberCandidate);
        joinGroupChat(user, chatToMemberCandidate.getChat());
    }

    /**
     * 用户拒绝群的邀请
     *
     * @param userId 传入的应该是用户的id
     * @return
     */
    public void rejectChatInvitation(Long userId, Long chatToMemberCandidateId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatToMemberCandidate chatToMemberCandidate = chatToMemberCandidateRepository.findById(chatToMemberCandidateId).orElseThrow(() -> new ResponseException(ErrorCode.INVITATION_NOT_EXIST));
        if (chatToMemberCandidate.getUser() != user)
            throw new ResponseException(ErrorCode.INVITATION_NOT_EXIST);
        if (chatToMemberCandidate.getStatus() != ChatToMemberCandidate.Status.PENDING)
            throw new ResponseException(ErrorCode.INVITATION_SOLVED);
        chatToMemberCandidate.setStatus(ChatToMemberCandidate.Status.REJECTED);
        chatToMemberCandidateRepository.save(chatToMemberCandidate);
    }

    /**
     * 群接受用户的申请
     *
     * @param userId 传入的应该是群管理员的id
     * @return
     */
    public void acceptChatApplication(Long userId, Long memberToChatCandidateId) {
        MemberToChatCandidate memberToChatCandidate = memberToChatCandidateRepository.findById(memberToChatCandidateId).orElseThrow(() -> new ResponseException(ErrorCode.GROUP_APPLICATION_NOT_EXIST));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId, memberToChatCandidate.getChat().getId())
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));

        memberToChatCandidate.setStatus(MemberToChatCandidate.Status.ACCEPTED);
        memberToChatCandidateRepository.save(memberToChatCandidate);
        joinGroupChat(memberToChatCandidate.getUser(), memberToChatCandidate.getChat());
    }

    /**
     * 群拒绝用户的申请
     *
     * @param userId 传入的应该是群管理员的id
     * @return
     */
    public void rejectChatApplication(Long userId, Long memberToChatCandidateId) {
        MemberToChatCandidate memberToChatCandidate = memberToChatCandidateRepository.findById(memberToChatCandidateId).orElseThrow(() -> new ResponseException(ErrorCode.GROUP_APPLICATION_NOT_EXIST));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId, memberToChatCandidate.getChat().getId())
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));

        memberToChatCandidate.setStatus(MemberToChatCandidate.Status.REJECTED);
        memberToChatCandidateRepository.save(memberToChatCandidate);
    }

    /**
     * 获取自己发给群的加群申请
     *
     * @param userId 自己的userId
     * @return
     */
    public List<MemberToChatCandidateResponse> getSelfGroupChatApplication(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var memberToChatCandidates = memberToChatCandidateRepository.findAllByUser_IdOrderByCreatedDate(userId);
        return memberToChatCandidates.stream().map(memberToChatCandidate -> {
            MemberToChatCandidateResponse memberToChatCandidateResponse = new MemberToChatCandidateResponse();
            memberToChatCandidateResponse.setId(memberToChatCandidate.getId());
            memberToChatCandidateResponse.setCreatedDate(memberToChatCandidate.getCreatedDate());
            memberToChatCandidateResponse.setUserId(memberToChatCandidate.getUser().getId());
            memberToChatCandidateResponse.setChatId(memberToChatCandidate.getChat().getId());
            memberToChatCandidateResponse.setMessage(memberToChatCandidate.getMessage());
            memberToChatCandidateResponse.setStatus(memberToChatCandidate.getStatus());
            return memberToChatCandidateResponse;
        }).toList();
    }

    /**
     * 获取群发给自己的加群申请
     *
     * @param userId 自己的userId
     * @return
     */
    public List<ChatToMemberCandidateResponse> getGroupChatApplication(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var chatToMemberCandidates = chatToMemberCandidateRepository.findAllByUser_IdOrderByCreatedDate(userId);
        return chatToMemberCandidates.stream().map(chatToMemberCandidate ->{
            ChatToMemberCandidateResponse chatToMemberCandidateResponse = new ChatToMemberCandidateResponse();
            chatToMemberCandidateResponse.setId(chatToMemberCandidate.getId());
            chatToMemberCandidateResponse.setCreatedDate(chatToMemberCandidate.getCreatedDate());
            chatToMemberCandidateResponse.setUserId(chatToMemberCandidate.getUser().getId());
            chatToMemberCandidateResponse.setIssuerId(chatToMemberCandidate.getIssuer().getId());
            chatToMemberCandidateResponse.setChatId(chatToMemberCandidate.getChat().getId());
            chatToMemberCandidateResponse.setMessage(chatToMemberCandidate.getMessage());
            chatToMemberCandidateResponse.setStatus(chatToMemberCandidate.getStatus());
            return chatToMemberCandidateResponse;
        }).toList();
    }
    /**
     * 获取群发给用户的邀请
     * @param userId 拥有管理员权限的用户的id
     * @param chatId 群聊的id
     */
    public List<ChatToMemberCandidateResponse> getGroupChatInvitation(Long userId,Long chatId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        var chatToMemberCandidates = chatToMemberCandidateRepository.findAllByChat_IdOrderByCreatedDate(chatId);
        return chatToMemberCandidates.stream().map(chatToMemberCandidate ->{
            ChatToMemberCandidateResponse chatToMemberCandidateResponse = new ChatToMemberCandidateResponse();
            chatToMemberCandidateResponse.setId(chatToMemberCandidate.getId());
            chatToMemberCandidateResponse.setCreatedDate(chatToMemberCandidate.getCreatedDate());
            chatToMemberCandidateResponse.setUserId(chatToMemberCandidate.getUser().getId());
            chatToMemberCandidateResponse.setIssuerId(chatToMemberCandidate.getIssuer().getId());
            chatToMemberCandidateResponse.setChatId(chatToMemberCandidate.getChat().getId());
            chatToMemberCandidateResponse.setMessage(chatToMemberCandidate.getMessage());
            chatToMemberCandidateResponse.setStatus(chatToMemberCandidate.getStatus());
            return chatToMemberCandidateResponse;
        }).toList();
    }
    /**
     * 获取用户发给群的邀请
     * @param userId 拥有管理员权限的用户的id
     * @param chatId 群聊的id
     */
    public List<MemberToChatCandidateResponse> getUserApplication(Long userId,Long chatId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        var memberToChatCandidates = memberToChatCandidateRepository.findAllByChat_IdOrderByCreatedDate(userId);
        return memberToChatCandidates.stream().map(memberToChatCandidate -> {
            MemberToChatCandidateResponse memberToChatCandidateResponse = new MemberToChatCandidateResponse();
            memberToChatCandidateResponse.setId(memberToChatCandidate.getId());
            memberToChatCandidateResponse.setCreatedDate(memberToChatCandidate.getCreatedDate());
            memberToChatCandidateResponse.setUserId(memberToChatCandidate.getUser().getId());
            memberToChatCandidateResponse.setChatId(memberToChatCandidate.getChat().getId());
            memberToChatCandidateResponse.setMessage(memberToChatCandidate.getMessage());
            memberToChatCandidateResponse.setStatus(memberToChatCandidate.getStatus());
            return memberToChatCandidateResponse;
        }).toList();
    }
    /**
     * 转让群主
     * @param ownerId 群主id
     * @param newOwnerId 被转让用户id
     */
    public void changeGroupOwner(Long ownerId,Long newOwnerId, Long chatId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User newOwner = userRepository.findById(newOwnerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!owner.isVerified() || !newOwner.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember ownerChatMember = chatMemberRepository.findByUser_IdAndChat_Id(ownerId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        ChatMember newchatMember = chatMemberRepository.findByUser_IdAndChat_Id(newOwnerId,chatId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_IN_GROUP));
        if (ownerId == newOwnerId)
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        ownerChatMember.setMemberType(ChatMember.MemberType.REGULAR_MEMBER);
        newchatMember.setMemberType(ChatMember.MemberType.GROUP_OWNER);
        chatMemberRepository.save(ownerChatMember);
        chatMemberRepository.save(newchatMember);
    }

    /**
     * 授予管理员权限
     *
     */
    public void grantAdministrator(Long ownerId,Long userId,Long chatId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User administrator = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!owner.isVerified() || !administrator.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember ownerChatMember = chatMemberRepository.findByUser_IdAndChat_Id(ownerId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        ChatMember newchatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_IN_GROUP));
        if (newchatMember.getMemberType() != ChatMember.MemberType.REGULAR_MEMBER)
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        newchatMember.setMemberType(ChatMember.MemberType.ADMINISTRATOR);
        chatMemberRepository.save(newchatMember);
    }
    /**
     * 移除管理员权限
     */
    public void removeAdministrator(Long ownerId,Long administratorId,Long chatId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User administrator = userRepository.findById(administratorId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!owner.isVerified() || !administrator.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember ownerChatMember = chatMemberRepository.findByUser_IdAndChat_Id(ownerId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        ChatMember newchatMember = chatMemberRepository.findByUser_IdAndChat_Id(administratorId,chatId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_IN_GROUP));
        if (newchatMember.getMemberType() != ChatMember.MemberType.ADMINISTRATOR)
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        newchatMember.setMemberType(ChatMember.MemberType.REGULAR_MEMBER);
        chatMemberRepository.save(newchatMember);
    }

    /**
     * 踢出群成员
     */
    public void removeMember(Long administratorId, Long userId, Long chatId) {
        User owner = userRepository.findById(administratorId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User administrator = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!owner.isVerified() || !administrator.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember ownerChatMember = chatMemberRepository.findByUser_IdAndChat_Id(administratorId, chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        ChatMember newchatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_IN_GROUP));
        if (newchatMember.getMemberType() != ChatMember.MemberType.REGULAR_MEMBER)
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        chatMemberRepository.delete(newchatMember);
    }

    /**
     * 解散群聊
     */
    public void deleteChat(Long ownerId,Long chatId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(ownerId,chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER)
                .orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
        var chatMembers = chatMemberRepository.findAllByChat_Id(chatId);
        for (ChatMember cm : chatMembers) {
            chatMemberRepository.delete(cm);
        }
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));
        chatRepository.delete(chat);
    }
    /**
    *   获取群信息
    * */
    public ChatResponse getGroupChat(Long userId,Long chatId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));
        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_IN_GROUP));
        return new ChatResponse(chat.getId(),chat.getName(),chat.getType(),null,null,chat.getCreatedDate());
    }
    /**
     *  获取群成员
     */
    public List<ChatMemberResponse> getChatMember(Long userId, Long chatId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));
        ChatMember cm = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_IN_GROUP));
        var ChatMembers = chatMemberRepository.findAllByChat_Id(chatId);
        return ChatMembers.stream().map(chatMember -> {
            UserData userData = userDataRepository.findByUser_Id(chatMember.getUser().getId()).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
            return new ChatMemberResponse(chatMember.getId(),chat.getName(),chatMember.getMemberType(),chat.getCreatedDate(),chatMember.getUser().getId(),userData.getUsername());
        }).toList();
    }

    /**
     * 获取自己所在的所有聊天
     */
    public List<ChatResponse> getAllChat(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        var chatMembers = chatMemberRepository.findAllByUser_Id(userId);
        return chatMembers.stream().map(chatMember -> {
            Chat chat = chatMember.getChat();
            return new ChatResponse(chat.getId(),chat.getName(),chat.getType(),chat.getUser1().get().getId(), chat.getUser2().get().getId(),chat.getCreatedDate());
        }).toList();
    }
}
