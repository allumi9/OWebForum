package com.oop.owebforum.controllers;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.services.PostService;
import com.oop.owebforum.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private PostService postService;
    private AppUserRepository appUserRepository;
    private PostRepository postRepository;

    @Autowired
    public PostController(PostService postService,
                          AppUserRepository appUserRepository,
                          PostRepository postRepository){
        this.appUserRepository = appUserRepository;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @GetMapping("/post/{id}")
    public String showPostById(@PathVariable Long id, Model model){
        Post post = postRepository.getReferenceById(id);

        model.addAttribute("postTitle", post.getTitle());
        model.addAttribute("op", post.getOriginalPoster().getUsername());
        model.addAttribute("content", post.getContent());
        model.addAttribute("createdAt", post.getCreatedAt().withNano(0).toString());
        return "show_post_by_id";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Post> recentPosts = postService.getAllPosts();
        model.addAttribute("posts", recentPosts);
        return "home";
    }

    @GetMapping("/post/new")
    public String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post_form";
    }

    @PostMapping("/post/new")
    public String createPost(@ModelAttribute Post post, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(userDetails.getUsername());
        if (optionalUser.isPresent()) {
            AppUser loggedInUser = optionalUser.get();
            postService.createPost(post, loggedInUser);
            return "redirect:/home";
        } else {
            return "redirect:/login?error";
        }
    }
}
