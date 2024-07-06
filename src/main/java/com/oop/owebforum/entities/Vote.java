package com.oop.owebforum.entities;

import com.oop.owebforum.enums.VoteState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Vote {
    @Id
    private Long id;

    private VoteState state;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "voter")
    private AppUser voter;

    @ManyToOne
    @JoinColumn(name="post", nullable = false)
    private Post post;

}
