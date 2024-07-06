package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.enums.VoteState;
import com.oop.owebforum.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }

    public VoteState getPostVoteStateForCurrentUser(AppUser appUser, Post post){
        if(voteRepository.findByVoterAndPost(appUser, post).isEmpty()){
            return null;
        }
        return voteRepository.findByVoterAndPost(appUser, post).get().getState();
    }

    public void changePostVoteState(VoteState newState, AppUser appUser, Post post) throws Exception {
        if(voteRepository.findByVoterAndPost(appUser, post).isEmpty()){
            throw new Exception("lol wha");
        }
        voteRepository.findByVoterAndPost(appUser, post).get().setState(newState);
        if(newState.equals(VoteState.UP)){
            post.setRating(post.getRating() + 1);
        }

        if(newState.equals(VoteState.DOWN)){
            post.setRating(post.getRating() - 1);
        }
    }


}
