package com.reddot.app.dto.response;

public class UserAnswerStatisticsDTO {
    private Long totalAnswers;

    public UserAnswerStatisticsDTO(Long totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public Long getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Long totalAnswers) {
        this.totalAnswers = totalAnswers;
    }
}
