package com.oop.owebforum.controllers;

import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.CategoryRepository;
import com.oop.owebforum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CategoriesController {

    private PostService postService;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoriesController(PostService postService,
                                                     CategoryRepository categoryRepository){
        this.postService = postService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/category/{categoryName}")
    public String showCategoryPosts(@PathVariable String categoryName, Model model){
        model.addAttribute("title", categoryName + " posts");
        if(categoryRepository.findByName(categoryName).isEmpty()){
            model.addAttribute("error", "Category not found");
            return "show_category_posts";
        }
        List<Post> posts = postService.getAllPostsByCategoryOrderByRatingDesc(categoryRepository.findByName(categoryName).get());
        model.addAttribute("posts", posts);

        return "show_category_posts";
    }

}
