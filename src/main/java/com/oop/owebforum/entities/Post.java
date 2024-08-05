package com.oop.owebforum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class Post implements VoteSubject{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser originalPoster;

    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Category category;

    private LocalDateTime createdAt;
    private int rating;

}
