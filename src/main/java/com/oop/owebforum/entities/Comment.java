package com.oop.owebforum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Comment implements VoteSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post originalPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    private String content;
    private int rating;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment originalComment;

    @OneToMany(mappedBy = "originalComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> replies;
}
