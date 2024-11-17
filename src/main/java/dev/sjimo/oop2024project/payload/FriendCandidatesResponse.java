package dev.sjimo.oop2024project.payload;

import dev.sjimo.oop2024project.model.FriendCandidate;

import java.util.List;

public class FriendCandidatesResponse {
    private List<FriendCandidateResponse> list;

    public void setList(List<FriendCandidateResponse> list) {
        this.list = list;
    }

    public List<FriendCandidateResponse> getList() {
        return list;
    }
}
