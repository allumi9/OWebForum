package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    private AppUserRepository appUserRepository;
    private PostRepository postRepository;

    @Autowired
    public ProfileController(AppUserRepository appUserRepository,
                             PostRepository postRepository){
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/profile/get/")
    public String redirectNotLoggedInUserToLogin(@AuthenticationPrincipal UserDetails userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        return "redirect:/profile/get/" + userDetails.getUsername();
    }

    @GetMapping("/profile/get/{username}")
    public String getUserProfile(@PathVariable String username,
                                 Model model){
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);

        if(appUser.isEmpty()){
            return "error";
        }
        model.addAttribute("username", username);
        model.addAttribute("dateOfRegistration", appUser.get().getDateOfRegistration());
        List<Post> posts = postRepository.findAllByOriginalPoster(appUser.get());
        model.addAttribute("posts", posts);
        model.addAttribute("karma", appUser.get().getKarma());

        return "profile_page";
    }

}
