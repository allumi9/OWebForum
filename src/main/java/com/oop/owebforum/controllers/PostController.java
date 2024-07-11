package com.oop.owebforum.controllers;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.repositories.CategoryRepository;
import com.oop.owebforum.repositories.CommentRepository;
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
    private CommentRepository commentRepository;

    @Autowired
    public PostController(PostService postService,
                          AppUserRepository appUserRepository,
                          PostRepository postRepository,
                          CategoryRepository categoryRepository,
                          CommentRepository commentRepository){
        this.appUserRepository = appUserRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/post/show/{id}")
    public String showPostById(@PathVariable Long id, Model model){
        Post post = postRepository.findById(id).get();
        List<Comment> comments = commentRepository.findAllByOriginalPost(post);

        model.addAttribute("comments", comments);
        model.addAttribute("postTitle", post.getTitle());
        model.addAttribute("op", post.getOriginalPoster().getUsername());
        model.addAttribute("content", post.getContent());
        model.addAttribute("createdAt", post.getCreatedAt().withNano(0).toString());
        model.addAttribute("category", post.getCategory().getName());
        model.addAttribute("rating", post.getRating());
        model.addAttribute("postid", id);

        return "show_post_by_id";
    }

    @GetMapping("/home")
    public String showHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Post> recentPosts = postService.getRecentPosts();

        if(userDetails != null){
            model.addAttribute("username", userDetails.getUsername());
        }

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
