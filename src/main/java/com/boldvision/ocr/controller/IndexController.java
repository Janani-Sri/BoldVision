package com.boldvision.ocr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* To map thyme templates */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/signin")
    public String signIn(){
        return "signin";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "signup";
    }

    @GetMapping("/uploadImage")
    public String uploadImage(){
        return "upload";
    }
}
