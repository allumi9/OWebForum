package com.oop.owebforum.services;

import com.oop.owebforum.entities.*;
import com.oop.owebforum.enums.VoteState;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.CommentRepository;
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
    private CommentService commentService;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                          PostRepository postRepository,
                          AppUserService appUserService,
                       PostService postService,
                       CommentService commentService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.appUserService = appUserService;
        this.postService = postService;
        this.commentService = commentService;
    }

    public void conductVotingAlgorithm(Optional<Vote> optionalVote,
                                             String action,
                                             VoteSubject voteSubject,
                                             Post post,
                                             AppUser appUser){
        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();
            if (vote.getState().equals(VoteState.UP)) {
                if (action.equalsIgnoreCase("up")) {
                    voteRepository.delete(vote);
                    voteSubject.setRating(voteSubject.getRating() - 1);
                } else {
                    vote.setState(VoteState.DOWN);
                    voteSubject.setRating(voteSubject.getRating() - 2);
                    voteRepository.save(vote);
                }
            } else if (vote.getState().equals(VoteState.DOWN)) {
                if (action.equalsIgnoreCase("down")) {
                    voteRepository.delete(vote);
                    voteSubject.setRating(voteSubject.getRating() + 1);
                } else {
                    vote.setState(VoteState.UP);
                    voteSubject.setRating(voteSubject.getRating() + 2);
                    voteRepository.save(vote);
                }
            }
        } else {
            Vote newVote = new Vote();
            if(voteSubject.getClass().equals(Comment.class)){
                newVote.setComment((Comment) voteSubject);
            }
            if(voteSubject.getClass().equals(Post.class)){
                newVote.setPost((Post) voteSubject);
            }

            newVote.setVoter(appUser);
            if (action.equalsIgnoreCase("up")) {
                newVote.setState(VoteState.UP);
                voteSubject.setRating(voteSubject.getRating() + 1);
            } else if (action.equalsIgnoreCase("down")) {
                newVote.setState(VoteState.DOWN);
                voteSubject.setRating(voteSubject.getRating() - 1);
            }
            voteRepository.save(newVote);
        }
    }

    public void changePostRating(Long id, String username, String action) throws Exception {
        Post post = postService.findByID(id);
        AppUser appUser = appUserService.loadUserByUsername(username);

        Optional<Vote> optionalVote = voteRepository.findByVoterAndPost(appUser, post);
        conductVotingAlgorithm(optionalVote, action, post, post, appUser);

        appUserService.updateAppUserKarma(post.getOriginalPoster());
        postRepository.save(post);
    }

    public void changeCommentRating(Long id, String username, String action, Long commentID) throws Exception {
        Post post = postService.findByID(id);
        AppUser appUser = appUserService.loadUserByUsername(username);
        Comment comment = commentService.findCommentById(commentID);

        Optional<Vote> optionalVote = voteRepository.findByVoterAndComment(appUser, comment);
        conductVotingAlgorithm(optionalVote, action, comment, post, appUser);

        appUserService.updateAppUserKarma(comment.getAppUser());
        commentService.save(comment);
    }
}
