package com.reddot.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serial;

/**
 * Vote entity
 * <p>
 * A vote can be on a question or a comment.
 * A vote can be an upvote or a downvote.
 * A vote is associated with a user.
 */
@Entity(name = "votes")
@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class Vote extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The question to vote
     * <p>
     * If the vote is on a question, this field is set.
     * If the vote is on a comment, this field is null.
     * This field is optional.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * The comment to vote
     * <p>
     * If the vote is on a comment, this field is set.
     * If the vote is on a question, this field is null.
     * This field is optional.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_type_id")
    @NonNull
    private VoteType voteType;

    public Vote(@NonNull User user, Question question, @NonNull VoteType voteType) {
        this.user = user;
        this.question = question;
        this.voteType = voteType;
    }

    public Vote(@NonNull User user, Comment comment, @NonNull VoteType voteType) {
        this.user = user;
        this.comment = comment;
        this.voteType = voteType;
    }

    public void setQuestion(Question question) {
        this.question = question;
        question.setVote(this);
    }
}
