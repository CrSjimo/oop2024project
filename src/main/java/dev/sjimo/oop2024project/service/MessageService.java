package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.Chat;
import dev.sjimo.oop2024project.model.Message;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.payload.MessageRequest;
import dev.sjimo.oop2024project.payload.MessageResponse;
import dev.sjimo.oop2024project.repository.ChatMemberRepository;
import dev.sjimo.oop2024project.repository.ChatRepository;
import dev.sjimo.oop2024project.repository.MessageRepository;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    private void checkIsMember(User user, Chat chat) {
        if (chat.getUser1().isPresent() && chat.getUser1().get().getId().equals(user.getId())) {
            return;
        }
        if (chat.getUser2().isPresent() && chat.getUser2().get().getId().equals(user.getId())) {
            return;
        }
        chatMemberRepository.findByUser_IdAndChat_Id(user.getId(), chat.getId()).orElseThrow(() -> new ResponseException(ErrorCode.PERMISSION_DENIED));
    }

    /**
     * 用户向chatroom发送消息
     *
     * @param userId
     * @param chatId
     * @param messageRequest
     */
    public void sendMessage(Long userId, Long chatId, MessageRequest messageRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        checkIsMember(user, chat);

        Message messageEntity = new Message();
        messageEntity.setChat(chat);
        messageEntity.setStatus(Message.Status.UNREAD);
        messageEntity.setUser(user);
        messageEntity.setMessage(messageRequest.getMessage());
        messageRepository.save(messageEntity);
    }

    /**
     * 消息撤回
     *
     * @param userId
     * @param messageId
     */
    public void revokeMessage(Long userId, Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Message message = messageRepository.findById(messageId).orElseThrow(() -> new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));

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
     * 获取指定的消息？
     *
     * @param userId
     * @param chatId
     */
    public List<MessageResponse> getMessage(Long userId, Long chatId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        checkIsMember(user, chat);
        var allMessages = messageRepository.findAllByUser_IdAndChat_Id(userId, chatId);
        return allMessages.stream().map(message -> {
            MessageResponse messageResponse = new MessageResponse(message.getId(), message.getMessage(), message.getUser(), message.getChat(), message.getCreatedDate(), message.getStatus());
            return messageResponse;
        }).toList();
    }

    /**
     * 获取最近的消息
     */
    public MessageResponse getLatestMessage(Long userId, Long chatId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResponseException(ErrorCode.CHAT_NOT_EXIST));

        checkIsMember(user, chat);
        var message = messageRepository.findLatestMessageIdByChatId(chatId);
        if (message.isEmpty()) return null;
        Message latestMessage = message.get();
        return new MessageResponse(latestMessage.getId(), latestMessage.getMessage(), latestMessage.getUser(), chat, latestMessage.getCreatedDate(), latestMessage.getStatus());
    }

    /**
     * 获取从这条信息开始的指定数量消息
     */
    public List<MessageResponse> getMessages(Long userId, Long messageId, Integer limit) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));
        Long chatId = message.getChat().getId();
        checkIsMember(user, message.getChat());
        if (limit < 0) throw new ResponseException(ErrorCode.MESSAGE_NOT_EXIST);
        Pageable pageable = PageRequest.of(0, limit); // limit 是你想要的条数
        var messages = messageRepository.findMessagesBefore(chatId, messageId, pageable);
        return messages.stream().map(m -> new MessageResponse(m.getId(), m.getMessage(), m.getUser(), m.getChat(), m.getCreatedDate(), m.getStatus())).toList();
    }

    /**
     * 获取比这条信息更新的消息
     */
    public List<MessageResponse> getMessagesAfter(Long userId, Long messageId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new ResponseException(ErrorCode.MESSAGE_NOT_EXIST));
        Long chatId = message.getChat().getId();
        checkIsMember(user, message.getChat());
        var messages = messageRepository.findMessagesAfter(chatId, messageId);
        return messages.stream().map(m -> new MessageResponse(m.getId(), m.getMessage(), m.getUser(), m.getChat(), m.getCreatedDate(), m.getStatus())).toList();
    }
}