package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.ChatMember;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.payload.MessageResponse;
import dev.sjimo.oop2024project.repository.ChatMemberRepository;
import dev.sjimo.oop2024project.repository.ChatRepository;
import dev.sjimo.oop2024project.repository.MessageRepository;
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
    MessageRepository messageRepository;

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

        Message messageEntity = new Message();
        messageEntity.setChat(chat);
        messageEntity.setStatus(Message.Status.UNREAD);
        messageEntity.setUser(user);
        messageEntity.setMessage(message);
        return messageRepository.save(messageEntity);
    }

    /**
     * 消息撤回
     * @param userId
     * @param messageId
     */
    public void revokeMessage(Long userId,Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Message message = messageRepository.findById(messageId).orElseThrow(()-> new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));

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
        //delete
        messageRepository.delete(message);
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

        Message messageEntity = new Message();
        messageEntity.setChat(chat);
        messageEntity.setStatus(Message.Status.UNREAD);
        messageEntity.setUser(user);
        messageEntity.setMessage(message);
        messageEntity.setCreatedDate(LocalDateTime.now());
        return messageRepository.save(messageEntity);
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

        Message message = messageRepository.findById(messageId).orElseThrow(()-> new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));
        if (!message.getChat().equals(chat)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        messageRepository.delete(message);
    }

    /**
     * 获取指定的消息？
     * @param userId
     * @param chatId
     * @param messageId
     */
    public MessageResponse getMessage(Long userId, Long chatId, Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified())
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        ChatMember chatMember = chatMemberRepository.findByUser_IdAndChat_Id(userId,chatId).orElseThrow(()-> new ResponseException(ErrorCode.PERMISSION_DENIED));
        Message message = messageRepository.findById(messageId).orElseThrow(()-> new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));

        if (!message.getChat().equals(chat)) {
            throw new ResponseException(ErrorCode.PERMISSION_DENIED);
        }
        return new MessageResponse(messageId,message.getMessage(),message.getUser(),chat,message.getCreatedDate(),message.getStatus());
    }
}