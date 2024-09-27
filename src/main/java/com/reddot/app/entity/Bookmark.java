package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Entity class for bookmarks
 * One user can have many bookmarks
 *
 * @author trongtoannguyen
 */

@Entity(name = "bookmarks")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NonNull
    private User user;

    /**
     * Question that user bookmarked.
     * Null if user bookmarked a comment
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * Comment that user bookmarked.
     * Null if user bookmarked a question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Bookmark(@NonNull User user, Question question) {
        this.user = user;
        this.question = question;
    }
}
