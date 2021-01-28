package com.example.demo.controller;

import com.example.demo.repository.AllianceRepository;
import com.example.demo.service.implement.AllianceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class MainController {
    @Autowired
    AllianceImpl alliance;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("all", alliance.getListY());
        return "home";
    }
}
