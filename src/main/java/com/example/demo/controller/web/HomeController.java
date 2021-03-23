package com.example.demo.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "user/index";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "user/alliance";
    }
}
