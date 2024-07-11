package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.dto.UserRegistrationDTO;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.CategoryRepository;
import com.oop.owebforum.repositories.CommentRepository;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class CommentController {

    private AppUserRepository appUserRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentController(AppUserRepository appUserRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository){
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("post/{postID}/comment")
    public String addComment(@PathVariable Long postID,
                             @RequestParam("comment-input") String input,
                             @AuthenticationPrincipal UserDetails userDetails){
        Comment comment = new Comment();
        AppUser appUser = appUserRepository.findByUsername(userDetails.getUsername()).get();
        Post post = postRepository.findById(postID).get();

        comment.setAppUser(appUser);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setContent(input);
        comment.setOriginalPost(post);
        commentRepository.save(comment);

        return "redirect:/post/show/" + postID;
    }

}
