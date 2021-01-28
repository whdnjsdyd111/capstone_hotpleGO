package com.example.demo.controller;

import com.example.demo.repository.AllianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
