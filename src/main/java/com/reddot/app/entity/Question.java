package com.reddot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reddot.app.entity.enumeration.VOTETYPE;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Entity(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Question extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String body;

    private String title;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    private int upvotes;

    private int downvotes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinTable(name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Vote> votes;

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setQuestion(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void setVote(Vote vote) {
        if (!this.votes.contains(vote)) {
            this.votes.add(vote);
            if (vote.getVoteType().getType() == VOTETYPE.UPVOTE) {
                this.upvotes++;
            } else {
                this.downvotes++;
            }
            vote.setQuestion(this);
        }
    }

    public void close() {
        this.closedAt = LocalDateTime.now();
    }

    public void open() {
        this.closedAt = null;
    }

    public boolean isClosed() {
        return this.closedAt != null;
    }

    public int getScore() {
        return this.upvotes * 3 - this.downvotes;
    }
}
