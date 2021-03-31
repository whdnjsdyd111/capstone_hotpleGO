package com.example.demo.controller.web;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService user;

    @GetMapping("/")
    public String index() {
        return "user/index";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "user/alliance";
    }

    @GetMapping("/setting")
    public String setting(Model model) {
        // TODO
        model.addAttribute("user", user.get("whdnjsdyd1112@gmail.com/U/GO"));
        return "user/userSetting";
    }

    @GetMapping("/mbti")
    public String mbti(Model model) {
        return "user/mbti";
    }

    @GetMapping("/taste")
    public String taste(Model model) {
        return "user/taste";
    }

    @GetMapping("/myCource")
    public String myCource(@RequestParam(value = "kind", defaultValue = "usingCource") String kind, Model model) {
        if (kind.equals("usingCource")) {
            return "user/usingCource";
        } else if (kind.equals("makeCource")){
            return "user/makeCource";
        } else {
            return "redirect:/myCource";
        }
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
