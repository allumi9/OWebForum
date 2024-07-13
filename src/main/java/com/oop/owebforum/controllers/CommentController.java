package com.oop.owebforum.controllers;

import com.oop.owebforum.services.CommentService;
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

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("post/{postID}/comment")
    public String addComment(@PathVariable Long postID,
                             @RequestParam("comment-input") String input,
                             @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        commentService.addComment(postID, input, userDetails.getUsername());

        return "redirect:/post/show/" + postID;
    }

}
