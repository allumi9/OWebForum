package com.oop.owebforum.entities;

import com.oop.owebforum.enums.VoteState;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private VoteState state;

    @ManyToOne
    @JoinColumn(name = "voter_id", referencedColumnName = "user_id", nullable = false)
    private AppUser voter;

    @ManyToOne
    @JoinColumn(name="post")
    private Post post;

    @ManyToOne
    @JoinColumn(name="comment")
    private Comment comment;

}
