package com.oop.owebforum.controllers;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.services.PostService;
import com.oop.owebforum.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AppUserRepository appUserRepository;
    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Post> recentPosts = postService.getRecentPosts(); // Assuming this method returns a list of posts sorted by date
        model.addAttribute("posts", recentPosts);
        return "home";  // Thymeleaf template for the home page
    }
    @GetMapping("/post/new")
    public String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post_form";
    }

    @PostMapping("/post/new")
    public String createPost(@ModelAttribute Post post, @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            AppUser loggedInUser = optionalUser.get();
            postService.createPost(post, loggedInUser);
            return "redirect:/posts";
        } else {
            return "redirect:/login?error";
        }
    }
}
