package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.model.BlockList;
import dev.sjimo.oop2024project.model.Friend;
import dev.sjimo.oop2024project.model.FriendCandidate;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.payload.BlockListResponse;
import dev.sjimo.oop2024project.payload.FriendCandidateResponse;
import dev.sjimo.oop2024project.payload.FriendResponse;
import dev.sjimo.oop2024project.repository.BlockListRepository;
import dev.sjimo.oop2024project.repository.FriendCandidateRepository;
import dev.sjimo.oop2024project.repository.FriendRepository;
import dev.sjimo.oop2024project.repository.UserRepository;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private BlockListRepository blockListRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private FriendCandidateRepository friendCandidateRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 将两个用户建立friend关系
     */
    private void beFriend(User user1, User user2) {
        Friend friend = new Friend();
        friend.setUser1(user1);
        friend.setUser2(user2);
        friendRepository.save(friend);
    }

    /**
     * 发送添加好友申请
     *
     * @param userId   发送申请的用户的id
     * @param friendId 希望成为好友的用户id
     */
    public void addFriend(Long userId, Long friendId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));

        if (userId.equals(friendId)) {
            throw new ResponseException(ErrorCode.FRIEND_CANNOT_SELF);
        }

        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        if (!friend.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }

        if (blockListRepository.existsByUser1_IdAndUser2_Id(userId, friendId)) {
            throw new ResponseException(ErrorCode.BLOCKED_BY_USER);
        }
        if (blockListRepository.existsByUser1_IdAndUser2_Id(friendId, userId)) {
            throw new ResponseException(ErrorCode.BLOCKED_BY_TARGET);
        }

        if (friendRepository.existsByUser1AndUser2(user, friend)) {
            throw new ResponseException(ErrorCode.ALREADY_BE_FRIEND);
        }

        if (friendCandidateRepository.existsByUser1_IdAndUser2_IdAndStatus(userId, friendId, FriendCandidate.Status.PENDING)) {
            throw new ResponseException(ErrorCode.FRIEND_APPLICATION_PENDING);
        }

        var friendCandidate = friendCandidateRepository.findByUser1_IdAndUser2_IdAndStatus(friendId, userId, FriendCandidate.Status.PENDING);
        if (friendCandidate.isPresent()) {
            friendCandidate.get().setStatus(FriendCandidate.Status.ACCEPTED);
            friendCandidateRepository.save(friendCandidate.get());
            beFriend(user, friend);
            return;
        }
        FriendCandidate friendCandidateEntry = new FriendCandidate();
        friendCandidateEntry.setUser1(user);
        friendCandidateEntry.setUser2(friend);
        friendCandidateEntry.setStatus(FriendCandidate.Status.PENDING);
        friendCandidateEntry.setMessage(message);
        friendCandidateRepository.save(friendCandidateEntry);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));

        if (userId.equals(friendId)) {
            throw new ResponseException(ErrorCode.FRIEND_CANNOT_SELF);
        }

        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        if (!friend.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var friendEntry = friendRepository.findByUser1AndUser2(user, friend);
        if (friendEntry.isEmpty()) {
            throw new ResponseException(ErrorCode.NOT_BE_FRIEND);
        }
        friendEntry.ifPresent(value -> friendRepository.delete(value));
    }

    public List<FriendResponse> listFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        var friends = friendRepository.listFriendsWithCollation(user.getId());
        return friends.stream().map(friend -> {
            var friendId = friend.getUser1().getId();
            if (friendId.equals(userId)) {
                friendId = friend.getUser2().getId();
            }
            return new FriendResponse(friendId, userId.equals(friend.getUser1().getId()) ? friend.getCommentName2() : friend.getCommentName1());
        }).toList();
    }

    public void blockOther(Long userId, Long otherId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User other = userRepository.findById(otherId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));

        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        if (!other.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var blockEntry = blockListRepository.findByUser1_IdAndUser2_Id(userId, otherId);
        if (blockEntry.isPresent()) {
            throw new ResponseException(ErrorCode.ALREADY_BLOCKED);
        }
        var friendEntry = friendRepository.findByUser1AndUser2(user, other);
        if (friendEntry.isPresent()) {
            deleteFriend(userId, otherId);
        }
        BlockList blockList = new BlockList();
        blockList.setUser1(user);
        blockList.setUser2(other);
        blockListRepository.save(blockList);
    }

    public void deleteBlock(Long userId, Long blockId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        User block = userRepository.findById(blockId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        if (!block.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var blockEntry = blockListRepository.findByUser1_IdAndUser2_Id(userId, blockId);
        if (!blockEntry.isPresent()) {
            throw new ResponseException(ErrorCode.NOT_BLOCKED);
        }
        blockListRepository.delete(blockEntry.get());
    }

    /**
     * 返回拉黑列表
     * @param userId
     * @return blockList
     */
    public List<BlockListResponse> getBlockList(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var blockLists = blockListRepository.findAllByUser1_IdOrderByCreatedDate(userId);
        return blockLists.stream().map(blockList -> {
            BlockListResponse blockListResponse = new BlockListResponse(blockList.getId(),
                    blockList.getUser1().getId(),
                    blockList.getUser2().getId(),
                    blockList.getCreatedDate());
            return blockListResponse;
        }).toList();
    }
    /**
     * 接受好友申请
     *
     * @param userId 传入处理申请的用户的id
     */
    public void acceptFriendApplication(Long userId, Long friendCandidateId) {
        var friendCandidate = friendCandidateRepository.findById(friendCandidateId).orElseThrow(() -> new ResponseException(ErrorCode.FRIEND_APPLICATION_NOT_EXIST));
        if (!friendCandidate.getUser2().getId().equals(userId)) {
            throw new ResponseException(ErrorCode.FRIEND_APPLICATION_NOT_EXIST);
        }
        if (friendCandidate.getStatus() != FriendCandidate.Status.PENDING) {
            throw new ResponseException(ErrorCode.FRIEND_APPLICATION_SOLVED);
        }
        friendCandidate.setStatus(FriendCandidate.Status.ACCEPTED);
        friendCandidateRepository.save(friendCandidate);
        beFriend(friendCandidate.getUser1(), friendCandidate.getUser2());
    }

    /**
     * 拒绝好友申请
     *
     * @param userId 传入处理申请的用户的id
     */
    public void rejectFriendApplication(Long userId, Long friendCandidateId) {
        var friendCandidate = friendCandidateRepository.findById(friendCandidateId).orElseThrow(() -> new ResponseException(ErrorCode.FRIEND_APPLICATION_NOT_EXIST));
        if (!friendCandidate.getUser2().getId().equals(userId)) {
            throw new ResponseException(ErrorCode.FRIEND_APPLICATION_NOT_EXIST);
        }
        if (friendCandidate.getStatus() != FriendCandidate.Status.PENDING) {
            throw new ResponseException(ErrorCode.FRIEND_APPLICATION_SOLVED);
        }
        friendCandidate.setStatus(FriendCandidate.Status.REJECTED);
        friendCandidateRepository.save(friendCandidate);
    }

    /**
     * 获取自己发给别人的好友申请
     *
     * @param userId 自己的userId
     * @return
     */
    public List<FriendCandidateResponse> getSelfFriendCandidates(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var friendCandidates = friendCandidateRepository.findAllByUser1_IdOrderByCreatedDate(userId);
        return friendCandidates.stream().map(friendCandidate -> {
            FriendCandidateResponse friendCandidateResponse = new FriendCandidateResponse();
            friendCandidateResponse.setId(friendCandidate.getId());
            friendCandidateResponse.setUserId(friendCandidate.getUser2().getId());
            friendCandidateResponse.setStatus(friendCandidate.getStatus());
            friendCandidateResponse.setCreatedDate(friendCandidate.getCreatedDate());
            friendCandidateResponse.setMessage(friendCandidate.getMessage());
            return friendCandidateResponse;
        }).toList();
    }

    /**
     * 获取别人发给自己的好友申请
     *
     * @param userId 自己的userId
     * @return
     */
    public List<FriendCandidateResponse> getOtherFriendCandidates(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (!user.isVerified()) {
            throw new ResponseException(ErrorCode.USER_NOT_VERIFIED);
        }
        var friendCandidates = friendCandidateRepository.findAllByUser2_IdOrderByCreatedDate(userId);
        return friendCandidates.stream().map(friendCandidate -> {
            FriendCandidateResponse friendCandidateResponse = new FriendCandidateResponse();
            friendCandidateResponse.setId(friendCandidate.getId());
            friendCandidateResponse.setUserId(friendCandidate.getUser1().getId());
            friendCandidateResponse.setStatus(friendCandidate.getStatus());
            friendCandidateResponse.setCreatedDate(friendCandidate.getCreatedDate());
            friendCandidateResponse.setMessage(friendCandidate.getMessage());
            return friendCandidateResponse;
        }).toList();
    }
}
