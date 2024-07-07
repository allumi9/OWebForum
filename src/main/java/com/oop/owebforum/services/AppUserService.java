package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService, IAppUserService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private PostRepository postRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder,
                          PostRepository postRepository){
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found.",username)));
    }
    @Override
    public AppUser saveAppUser(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    public void updateAppUserKarma(AppUser appUser){
        List<Post> posts = postRepository.findAllByOriginalPoster(appUser);
        int karma = 0;

        for(Post post:posts){
            karma += post.getRating();
        }
        appUser.setKarma(karma);
    }

}
