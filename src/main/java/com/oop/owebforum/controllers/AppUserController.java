package com.oop.owebforum.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class AppUserController {

    @GetMapping("/user")
    public String user(){
        return "User access level";
    }

}
