package com.reddot.app.dto.response;

public class UserVoteStatisticsDTO {
    private Long upvotes;
    private Long downvotes;

    public UserVoteStatisticsDTO(Long upvotes, Long downvotes) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Long upvotes) {
        this.upvotes = upvotes;
    }

    public Long getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Long downvotes) {
        this.downvotes = downvotes;
    }
}
