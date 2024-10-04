package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.VOTETYPE;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "questions")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Question extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NonNull
    private String body;

    @NonNull
    @Size(min = 3, max = 100)
    private String title;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    private int upvotes;

    private int downvotes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinTable(name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

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
        comment.setQuestion(null);
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
        return this.upvotes - this.downvotes;
    }
}
