package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    /** Идентификатор пользователя */
    @Id
    @Column(name = "id")
    private long id;

    /** Имя пользователя */
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<PostComment> comments;

    public User() {}
    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Идентификатор пользователя: " + id
                + "; Имя пользователя: " + name
                ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return id == user.id;
        }
        return false;
    }
}
