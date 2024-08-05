package com.oop.owebforum.controllers;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.repositories.CategoryRepository;

import com.oop.owebforum.services.AppUserService;
import com.oop.owebforum.services.PostService;
import com.oop.owebforum.repositories.AppUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PostController {

    private PostService postService;
    private AppUserService appUserService;
    private CategoryRepository categoryRepository;

    @Autowired
    public PostController(PostService postService,
                          AppUserService appUserService,
                          CategoryRepository categoryRepository){
        this.appUserService = appUserService;
        this.postService = postService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/post/show/{id}")
    public String showPostById(@PathVariable Long id, Model model) throws Exception {
        postService.makePostModelToShow(model, id);
        return "show_post_by_id";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        postService.makePostModelsToShowHome(model);
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
        AppUser appUser = appUserService.loadUserByUsername(userDetails.getUsername());

        Optional<Category> categoryOptional = categoryRepository.findByName(formCategoryName);
        if (categoryOptional.isPresent()) {
            post.setCategory(categoryOptional.get());
        } else {
            return "redirect:/post/new?error=categoryNotFound";
        }

        postService.createPost(post, appUser);
        return "redirect:/home";
    }

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(1L, "Music"));
            categoryRepository.save(new Category(2L, "Games"));
            categoryRepository.save(new Category(3L, "Literature"));
            categoryRepository.save(new Category(4L, "Movies"));
            categoryRepository.save(new Category(5L, "Science"));
            categoryRepository.save(new Category(6L, "Politics"));
            categoryRepository.save(new Category(7L, "Sports"));
            categoryRepository.save(new Category(8L, "Pets"));
            categoryRepository.save(new Category(9L, "Other"));
        }
    }

}
