package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.*;
import dev.sjimo.oop2024project.payload.ChatResponse;
import dev.sjimo.oop2024project.repository.*;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return new ChatResponse(chat.getId(), chat.getName(), chat.getType(), chat.getUser1().map(User::getId).orElse(null), chat.getUser2().map(User::getId).orElse(null));
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
        return new ChatResponse(chat.getId(), chat.getName(), chat.getType(), null, null);
    }

    public void sendInvitation(Long issuerId, Long userId,Long chatId,String message) {
        User sender = userRepository.findById(issuerId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));//判读发送者是否存在
        if (!sender.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(issuerId,chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.Administrator)
                .orElseThrow(()->new ResponseException(ErrorCode.PERMISSION_DENIED));

        if (chat.getType()!=Chat.Type.GROUP_CHAT)
            throw new ResponseException(ErrorCode.CHAT_NOT_GROUP);

        if (chatToMemberCandidateRepository.existsByUser_IdAndChat_IdAndStatus(userId, chatId, ChatToMemberCandidate.Status.PENDING)) {
            throw new ResponseException(ErrorCode.INVITAIOIN_ALREADY_EXIST);
        }

        if (chatMemberRepository.existsByUser_IdAndChat_Id(chatId, userId)) {
            throw new ResponseException(ErrorCode.USER_ALREADY_EXIST);
        }

        var memberToChatCandidate = memberToChatCandidateRepository.findByUser_IdAndChat_Id(userId, chatId);
        if (memberToChatCandidate.isPresent()) {
            ChatMember chatMemberEntity = new ChatMember();
            chatMemberEntity.setChat(chat);
            chatMemberEntity.setUser(user);
            chatMemberEntity.setMemberType(ChatMember.MemberType.REGULAR_MEMBER);
            chatMemberRepository.save(chatMemberEntity);
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
    public void ApplyToJoinGroup(Long userId,Long chatId,String message){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()){
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        if (chat.getType()!=Chat.Type.GROUP_CHAT)
            throw new ResponseException(ErrorCode.CHAT_NOT_GROUP);

        if (chatToMemberCandidateRepository.existsByUser_IdAndChat_IdAndStatus(userId, chatId, ChatToMemberCandidate.Status.PENDING)) {
            throw new ResponseException(ErrorCode.INVITAIOIN_ALREADY_EXIST);
        }

        if (chatMemberRepository.existsByUser_IdAndChat_Id(chatId, userId)) {
            throw new ResponseException(ErrorCode.USER_ALREADY_EXIST);
        }

        var chatToMemberCandidate = chatToMemberCandidateRepository.findByUser_IdAndChat_Id(userId, chatId);
        if (chatToMemberCandidate.isPresent()) {
            ChatMember chatMemberEntity = new ChatMember();
            chatMemberEntity.setChat(chat);
            chatMemberEntity.setUser(user);
            chatMemberEntity.setMemberType(ChatMember.MemberType.REGULAR_MEMBER);
            chatMemberRepository.save(chatMemberEntity);
            return;
        }
        MemberToChatCandidate memberToChatCandidateEntity = new MemberToChatCandidate();
        memberToChatCandidateEntity.setChat(chat);
        memberToChatCandidateEntity.setUser(user);
        memberToChatCandidateEntity.setStatus(MemberToChatCandidate.Status.PENDING);
        memberToChatCandidateRepository.save(memberToChatCandidateEntity);
    }
}
