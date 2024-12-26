package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Comment extends BaseEntity {

    private String text;
    private int upvotes;
    private int downvotes;

    /**
     * The question that this comment is related to
     * Never null
     */
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false) // prevent equals and hashCode breaking
    private Question question;

    /**
     * The comment that this comment is a response to
     * Null if this comment responds to a question
     */
    @ManyToOne
    @JoinColumn(name = "response_to")
    private Comment responseTo;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    public Comment(String body, User author) {
        this.text = body;
        this.user = author;
    }

    public void respondToComment(Comment comment) {
        this.responseTo = comment;
    }

    public int getScore() {
        return upvotes * 3 - downvotes;
    }
}
