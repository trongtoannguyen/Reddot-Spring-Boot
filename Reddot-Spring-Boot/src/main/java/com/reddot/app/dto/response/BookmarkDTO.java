package com.reddot.app.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookmarkDTO {
    private Integer id;
    private String description;
    private String title;
    private String username;
    private String createdAt;
    private String updatedAt;
}
