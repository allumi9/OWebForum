package com.oop.owebforum.controllers;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.repositories.CategoryRepository;
import com.oop.owebforum.repositories.PostRepository;

import com.oop.owebforum.services.PostService;
import com.oop.owebforum.repositories.AppUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private PostService postService;
    private AppUserRepository appUserRepository;
    private PostRepository postRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public PostController(PostService postService,
                          AppUserRepository appUserRepository,
                          PostRepository postRepository,
                          CategoryRepository categoryRepository){
        this.appUserRepository = appUserRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/post/show/{id}")
    public String showPostById(@PathVariable Long id, Model model){
        Post post = postRepository.getReferenceById(id);

        model.addAttribute("postTitle", post.getTitle());
        model.addAttribute("op", post.getOriginalPoster().getUsername());
        model.addAttribute("content", post.getContent());
        model.addAttribute("createdAt", post.getCreatedAt().withNano(0).toString());
        model.addAttribute("category", post.getCategory().getName());
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
    public String createPost(@ModelAttribute Post post,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam(value = "formCategoryName") String formCategoryName) {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(userDetails.getUsername());
        if (optionalUser.isPresent()) {
            AppUser loggedInUser = optionalUser.get();

            Optional<Category> categoryOptional = categoryRepository.findByName(formCategoryName);
            if (categoryOptional.isPresent()) {
                post.setCategory(categoryOptional.get());
            } else {
                return "redirect:/post/new?error=categoryNotFound";
            }

            postService.createPost(post, loggedInUser);
            return "redirect:/home";
        } else {
            return "redirect:/login?error";
        }
    }

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(null, "Music"));
            categoryRepository.save(new Category(null, "Games"));
            categoryRepository.save(new Category(null, "Literature"));
            categoryRepository.save(new Category(null, "Movies"));
            categoryRepository.save(new Category(null, "Science"));
            categoryRepository.save(new Category(null, "Politics"));
            categoryRepository.save(new Category(null, "Sports"));
            categoryRepository.save(new Category(null, "Pets"));
            categoryRepository.save(new Category(null, "Other"));
        }
    }

}
