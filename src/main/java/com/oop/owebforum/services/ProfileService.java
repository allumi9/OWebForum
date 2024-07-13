package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private AppUserService appUserService;
    private PostRepository postRepository;

    @Autowired
    public ProfileService(AppUserService appUserService,
                          PostRepository postRepository){
        this.appUserService = appUserService;
        this.postRepository = postRepository;
    }

    public void getUserProfile(String username,
                               Model model){
        AppUser appUser = appUserService.loadUserByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("dateOfRegistration", appUser.getDateOfRegistration());
        List<Post> posts = postRepository.findAllByOriginalPosterOrderByRatingDesc(appUser);
        model.addAttribute("posts", posts);
        model.addAttribute("karma", appUser.getKarma());
    }

}
