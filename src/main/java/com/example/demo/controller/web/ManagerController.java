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
        return "";
    }

    @GetMapping("/enrollment")
    public String enrollment() {
        return "manager/enrollment";
    }

    @GetMapping("/temporaryStop")
    public String temporaryStop() {
        return "manager/temporaryStop";
    }

    @GetMapping("/menuManagement")
    public String menuManagement() {
        return "manager/menuManagement";
    }

    @GetMapping("/test")
    public String test() {
        return "manager/test";
    }

    @GetMapping("/test2")
    public String test2() {
        return "manager/test2";
    }
}
