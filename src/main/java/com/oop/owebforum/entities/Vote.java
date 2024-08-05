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
    private Long id;

    @Enumerated(EnumType.STRING)
    private VoteState state;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser voter;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment comment;

}
