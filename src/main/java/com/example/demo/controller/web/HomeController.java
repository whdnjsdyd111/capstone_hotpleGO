package com.example.demo.controller.web;

import com.example.demo.service.web.AllianceService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {
    @Setter(onMethod_ = @Autowired)
    AllianceService alliance;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String home() {
        return "home";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "alliance";
    }



}
