package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.Vote;
import com.oop.owebforum.enums.VoteState;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VoteController {

    private final VoteRepository voteRepository;
    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;

    @Autowired
    public VoteController(VoteRepository voteRepository,
                          AppUserRepository appUserRepository,
                          PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/post/{id}/vote")
    public String changePostRating(@RequestParam("btn") String action,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable Long id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("Post not found"));
        AppUser appUser = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        Optional<Vote> optionalVote = voteRepository.findByVoterAndPost(appUser, post);
        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();
            if (vote.getState().equals(VoteState.UP)) {
                if (action.equalsIgnoreCase("up")) {
                    voteRepository.delete(vote);
                    post.setRating(post.getRating() - 1);
                } else {
                    vote.setState(VoteState.DOWN);
                    post.setRating(post.getRating() - 2);
                    voteRepository.save(vote);
                }
            } else if (vote.getState().equals(VoteState.DOWN)) {
                if (action.equalsIgnoreCase("down")) {
                    voteRepository.delete(vote);
                    post.setRating(post.getRating() + 1);
                } else {
                    vote.setState(VoteState.UP);
                    post.setRating(post.getRating() + 2);
                    voteRepository.save(vote);
                }
            }
        } else {
            Vote newVote = new Vote();
            newVote.setPost(post);
            newVote.setVoter(appUser);
            if (action.equalsIgnoreCase("up")) {
                newVote.setState(VoteState.UP);
                post.setRating(post.getRating() + 1);
            } else if (action.equalsIgnoreCase("down")) {
                newVote.setState(VoteState.DOWN);
                post.setRating(post.getRating() - 1);
            }
            voteRepository.save(newVote);
        }

        postRepository.save(post);
        return "redirect:/post/show/" + post.getId();
    }
}
