package com.reddot.app.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
@EqualsAndHashCode
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Question that user bookmarked.
     * Can not be null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    /**
     * Comment that user bookmarked in this case, question is parent of comment
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
