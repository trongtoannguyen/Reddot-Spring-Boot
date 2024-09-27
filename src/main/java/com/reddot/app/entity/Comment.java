package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "response_to")
    private Comment responseTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User user;


    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    public void respondToComment(Comment comment) {
        this.responseTo = comment;
    }
}
