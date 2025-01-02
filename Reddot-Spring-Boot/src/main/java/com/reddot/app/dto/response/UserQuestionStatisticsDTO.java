package com.reddot.app.dto.response;

public class UserQuestionStatisticsDTO {
    private Long totalQuestions;

    public UserQuestionStatisticsDTO(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
