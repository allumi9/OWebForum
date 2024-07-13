package com.oop.owebforum.controllers;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.repositories.CategoryRepository;

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
    private AppUserRepository appUserRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public PostController(PostService postService,
                          AppUserRepository appUserRepository,
                          CategoryRepository categoryRepository){
        this.appUserRepository = appUserRepository;
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
        // Вся логіка тут, бо перевірки використовують редіректи шо сервіс робити не може
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
