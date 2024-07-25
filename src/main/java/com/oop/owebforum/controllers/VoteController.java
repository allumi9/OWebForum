package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.Vote;
import com.oop.owebforum.enums.VoteState;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.repositories.VoteRepository;
import com.oop.owebforum.services.AppUserService;
import com.oop.owebforum.services.PostService;
import com.oop.owebforum.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VoteController {

    private VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/post/{id}/vote")
    public String changePostRating(@RequestParam("btn") String action,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable Long id) throws Exception {
        voteService.changePostRating(id, userDetails.getUsername(), action);

        return "redirect:/post/show/" + id;
    }

    @PostMapping("/post/{id}/vote/comment/{commentID}")
    public String changeCommentRating(@RequestParam("btn-comment") String action,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable Long id, @PathVariable Long commentID) throws Exception {
        voteService.changeCommentRating(id, userDetails.getUsername(), action, commentID);

        return "redirect:/post/show/" + id;
    }
}
