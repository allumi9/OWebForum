package com.oop.owebforum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoriesOverviewController {

    @GetMapping("/categories")
    public String showCategories(){
        return "categories_overview";
    }

}
