package com.cooksys.twitterapi.entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "user_table")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Timestamp joined = Timestamp.valueOf(LocalDateTime.now());

    @Column(nullable = false)
    private boolean deleted = false;

    @Embedded
    private Credentials credentials;

    @Embedded
    private Profile profile;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany(mappedBy = "mentions")
    private List<Tweet> mentioned;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;

    @ManyToMany
    @JoinTable
    private List<User> following;

    @ManyToMany(mappedBy = "likes")
    private List<Tweet> liked;

}
