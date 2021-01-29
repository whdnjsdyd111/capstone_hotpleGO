package com.example.demo.controller.web;

import com.example.demo.domain.AllianceVO;
import com.example.demo.service.implement.AllianceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    AllianceImpl service;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String home() {
        return "home";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "alliance";
    }



}
