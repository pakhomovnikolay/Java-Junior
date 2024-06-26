package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    /** Идентификатор публикации */
    @Id
    @Column(name = "id")
    private long id;

    /** Заголовок публикации */
    @Column(name = "title")
    private String title;

    /** Дата публикации */
    @Column(name = "date")
    private Date timeStamp;

    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post() {}
    public Post(long id, String title, Date date) {
        this.id = id;
        this.title = title;
        timeStamp = date;
    }

    @Override
    public String toString() {
        return "Идентификатор пользователя: " + user.getId()
                + "; Номер публикации: " + id
                + "; Дата публикации: " + timeStamp
                + "; Заголовок публикации: " + title
                ;
    }
}
