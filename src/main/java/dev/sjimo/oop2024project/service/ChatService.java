package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.ChatMember;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.payload.ChatResponse;
import dev.sjimo.oop2024project.repository.ChatMemberRepository;
import dev.sjimo.oop2024project.repository.ChatRepository;
import dev.sjimo.oop2024project.repository.FriendRepository;
import dev.sjimo.oop2024project.repository.UserRepository;
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

}
