package com.example.demo.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager/*")
public class ManagerController {

    @GetMapping("/register")
    public String registerManager() {
        return "";
    }

    @GetMapping("/login")
    public String login() {
        return "manager/login";
    }

    @GetMapping("/enrollment")
    public String enrollment() {
        return "manager/enrollment";
    }

}
