package com.reddot.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class VoteDto {

    @NotBlank(message = "Vote type cannot be null or blank")
    @JsonProperty("voteType") // Chỉ định key JSON tương ứng
    private String voteType;

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
}
