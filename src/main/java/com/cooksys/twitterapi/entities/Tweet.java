package com.cooksys.twitterapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private User author;

    @Column(nullable = false)
    private boolean deleted = false;

    @CreationTimestamp
    private Timestamp posted = Timestamp.valueOf(LocalDateTime.now());

    private String content;

    @ManyToOne
    @JoinColumn
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts;

    @ManyToOne
    @JoinColumn
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies;

    @ManyToMany
    @JoinTable
    private List<Hashtag> hashtags;

    @ManyToMany
    @JoinTable
    private List<User> likes;

    @ManyToMany
    @JoinTable
    private List<User> mentions;

}
