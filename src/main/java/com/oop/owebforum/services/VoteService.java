package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.Vote;
import com.oop.owebforum.enums.VoteState;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private VoteRepository voteRepository;
    private PostRepository postRepository;
    private AppUserService appUserService;
    private PostService postService;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                          PostRepository postRepository,
                          AppUserService appUserService,
                       PostService postService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.appUserService = appUserService;
        this.postService = postService;
    }

    public void changePostRating(Long id, String username, String action) throws Exception {
        Post post = postService.findByID(id);
        AppUser appUser = appUserService.loadUserByUsername(username);

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

        appUserService.updateAppUserKarma(post.getOriginalPoster());
        postRepository.save(post);
    }
}
