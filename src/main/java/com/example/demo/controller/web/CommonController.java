package com.example.demo.controller.web;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
public class CommonController {
    @GetMapping("/accessError")
    public String accessDenied(Authentication auth, Model model) {
        log.info("access Denied: " + auth);

        model.addAttribute("msg", "Access Denied : " + auth.getAuthorities().toArray()[0] + " 권한은 접근 불가");

        return "accessError";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutGET() {
        log.info("custom logout");

        return "/";
    }

    @PostMapping("/logout")
    public String logoutPost() {
        log.info("post custom logout");

        return "/";
    }
}
