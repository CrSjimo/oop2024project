package dev.sjimo.oop2024project.payload;

public class FriendResponse {
    private Long friendId;
    private String commentName;
    public FriendResponse(Long friendId, String commentName) {
        this.friendId = friendId;
        this.commentName = commentName;
    }

    public Long getFriendId() {
        return friendId;
    }

    public String getCommentName() {
        return commentName;
    }
}
