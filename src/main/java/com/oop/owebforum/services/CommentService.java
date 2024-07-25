package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.CommentRepository;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private AppUserRepository appUserRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private AppUserService appUserService;
    private PostService postService;

    @Autowired
    public CommentService(AppUserRepository appUserRepository,
                             PostRepository postRepository,
                             CommentRepository commentRepository,
                          AppUserService appUserService,
                          PostService postService){
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.appUserService = appUserService;
        this.postService = postService;
    }

    public Comment findCommentById(Long ID) throws Exception{
        if(commentRepository.findById(ID).isEmpty()){
            throw new Exception("Comment not found.");
        }
        return commentRepository.findById(ID).get();
    }

    public void addComment(Long postID,
                           String input,
                           String username) throws Exception{
        Comment comment = new Comment();
        AppUser appUser = appUserService.loadUserByUsername(username);
        Post post = postService.findByID(postID);

        comment.setAppUser(appUser);
        comment.setCreatedAt(LocalDateTime.now().withNano(0));
        comment.setContent(input);
        comment.setOriginalPost(post);
        comment.setRating(0);
        commentRepository.save(comment);
    }

    public void save(Comment comment){
        commentRepository.save(comment);
    }



}
