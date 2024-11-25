package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.ChatMember;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.repository.ChatMemberRepository;
import dev.sjimo.oop2024project.repository.ChatRepository;
import dev.sjimo.oop2024project.repository.MessageListRepository;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    MessageListRepository messageListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    /**
     * 用户向chatroom发送消息
     * @param userId
     * @param chatId
     * @param message
     */
    public Message sendMessage(Long userId,Long chatId,String message) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId)
                .orElseThrow(()-> new ResponseException(ErrorCode.PERMISSION_DENIED));

        Message messageEitity = new Message();
        messageEitity.setChat(chat);
        messageEitity.setStatus(Message.Status.UNREAD);
        messageEitity.setUser(user);
        messageEitity.setMessage(message);
        return messageListRepository.save(messageEitity);
    }

    /**
     * 消息撤回
     * @param userId
     * @param chatId
     * @param messageId
     */
    public void revokeMessage(Long userId,Long chatId,Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId)
                .orElseThrow(()-> new ResponseException(ErrorCode.PERMISSION_DENIED));

        // 检查消息是否存在
        Optional<Message> optionalMessage = messageListRepository.findByMessage_IdAndChat_Id(messageId,chatId);

        if (!optionalMessage.isPresent()) {
            throw new ResponseException(ErrorCode.MESSAGE_NOT_EXIST);
        }

        Message message = optionalMessage.get();
        //验证发送者
        if (!message.getUser().equals(user)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        //验证时间
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(message.getCreatedDate(), now);
        long minutes = duration.toMinutes();

        if (minutes >= 1) {
            throw new ResponseException(ErrorCode.MESSAGE_TOO_OLD);
        }
        //delelte
        messageListRepository.delete(message);
    }

    /**
     * 群聊管理员发布群公告
     * @param userId
     * @param chatId
     * @param message
     */
    public Message sendAnnouncement(Long userId,Long chatId,String message) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(()-> new ResponseException(ErrorCode.PERMISSION_DENIED));

        Message messageEitity = new Message();
        messageEitity.setChat(chat);
        messageEitity.setStatus(Message.Status.UNREAD);
        messageEitity.setUser(user);
        messageEitity.setMessage(message);
        messageEitity.setCreatedDate(LocalDateTime.now());
        return messageListRepository.save(messageEitity);
    }

    /**
     * 撤销群公告
     * @param userId
     * @param chatId
     * @param messageId
     */
    public void revokeGroupAnnouncement(Long userId,Long chatId,Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(()-> new ResponseException(ErrorCode.PERMISSION_DENIED));
        if (messageListRepository.existsByMessage_IdAndChat_IdAndSattus(messageId,chatId)){
            throw new ResponseException(ErrorCode.MESSAGE_ALREADY_EXIST);
        }

        Optional<Message> optionalMessage = messageListRepository.findByMessage_IdAndChat_Id(messageId,chatId);

        if (optionalMessage.isPresent()){
            messageListRepository.delete(optionalMessage.get());
        }else {
            throw new ResponseException(ErrorCode.MESSAGE_NOT_EXIST);
        }
    }

    /**
     * 获取消息
     * @param userId
     * @param chatId
     * @param messageId
     */
    public Message getMessage(Long userId, Long chatId, Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId)
                .filter(cm -> cm.getMemberType() == ChatMember.MemberType.GROUP_OWNER || cm.getMemberType() == ChatMember.MemberType.ADMINISTRATOR)
                .orElseThrow(()-> new ResponseException(ErrorCode.PERMISSION_DENIED));
        return messageListRepository.findByMessage_IdAndChat_Id(messageId,chatId)
                .orElseThrow(()->new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));
    }
}