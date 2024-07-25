package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.CommentRepository;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private AppUserRepository appUserRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository){
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found.",username)));
    }
    public void updateAppUserKarma(AppUser appUser){
        List<Post> posts = postRepository.findAllByOriginalPoster(appUser);
        List<Comment> comments = commentRepository.findAllByAppUser(appUser);
        int karma = 0;

        for(Post post:posts){
            karma += post.getRating();
        }
        for(Comment comment : comments){
            karma += comment.getRating();
        }
        appUser.setKarma(karma);
    }

}
