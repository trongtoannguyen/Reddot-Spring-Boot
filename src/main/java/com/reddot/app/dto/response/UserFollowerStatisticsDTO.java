package com.reddot.app.dto.response;

public class UserFollowerStatisticsDTO {
    private Long totalFollowers;

    public UserFollowerStatisticsDTO(Long totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public Long getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Long totalFollowers) {
        this.totalFollowers = totalFollowers;
    }
}
