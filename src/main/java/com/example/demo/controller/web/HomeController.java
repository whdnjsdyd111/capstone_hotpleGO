package com.example.demo.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "alliance";
    }

    @GetMapping("/popup/jusoPopup")
    public String popup() {
        return "manager/jusoPopup";
    }

    @GetMapping("/enrollment")
    public String enrollment() {
        return "enrollment";
    }
}
