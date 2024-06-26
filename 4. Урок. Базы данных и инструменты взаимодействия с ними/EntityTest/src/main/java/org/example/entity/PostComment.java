package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class PostComment {

    /** Идентификатор комментария */
    @Id
    @Column(name = "id")
    private long id;

    /** Текст комментария */
    @Column(name = "comment")
    private String comment;

    /** Дата публикации */
    @Column(name = "date")
    private Date timeStamp;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PostComment() {}
    public PostComment(long id, String comment, Date date) {
        this.id = id;
        this.comment = comment;
        timeStamp = date;
    }

    @Override
    public String toString() {
        return "Номер публикации: " + post.getId()
                + "; Номер комментария: " + id
                + "; Дата комментария: " + timeStamp
                + "; Текст комментария: " + comment
                ;
    }
}
