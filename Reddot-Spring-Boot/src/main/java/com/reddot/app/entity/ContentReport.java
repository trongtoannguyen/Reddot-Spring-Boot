package com.reddot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "content_reports")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentReport extends BaseEntity {

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "comment_id")
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
