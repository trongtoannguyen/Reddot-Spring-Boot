package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "comments")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @NonNull
    private String text;

    /**
     * The question that this comment is related to
     * Never null
     */
    @ManyToOne
    @JoinColumn(name = "question_id")
    @NonNull
    private Question question;

    /**
     * The comment that this comment is a response to
     * Null if this comment responds to a question
     */
    @ManyToOne
    @JoinColumn(name = "response_to")
    private Comment responseTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @NonNull
    private User user;


    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    public void respondToComment(Comment comment) {
        this.responseTo = comment;
    }
}
