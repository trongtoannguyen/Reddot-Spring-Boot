package com.reddot.app.dto.response;

public class UserBadgeStatisticsDTO {
    private Long totalBadges;

    public UserBadgeStatisticsDTO(Long totalBadges) {
        this.totalBadges = totalBadges;
    }

    public Long getTotalBadges() {
        return totalBadges;
    }

    public void setTotalBadges(Long totalBadges) {
        this.totalBadges = totalBadges;
    }
}
