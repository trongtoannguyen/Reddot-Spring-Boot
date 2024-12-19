package com.reddot.app.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopQuestionByTagDTO {
    // Getters and setters
    private Long questionId;
    private String questionTitle;
    private Long voteScore;
    private String tagName;

    public TopQuestionByTagDTO(Long questionId, String questionTitle, Long voteScore, String tagName) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.voteScore = voteScore;
        this.tagName = tagName;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setVoteScore(Long voteScore) {
        this.voteScore = voteScore;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
