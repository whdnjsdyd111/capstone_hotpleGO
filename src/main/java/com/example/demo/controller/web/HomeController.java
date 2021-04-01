package com.example.demo.controller.web;

import com.example.demo.security.CustomUser;
import com.example.demo.service.TasteService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {
    private final UserService user;
    private final TasteService taste;

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
    public String taste(@RequestParam(value = "required", required = false) String required, Model model,
                        @AuthenticationPrincipal CustomUser users) {
        if (required != null) {
            model.addAttribute("msg", "취향 선택 후 코스를 이용해 주십시오.");
        }
        model.addAttribute("tastes", taste.getTasteList(users.getUsername() + "/" + users.getAuthorities().toArray()[0] + "/"));
        return "user/taste";
    }

    @GetMapping("/myCourse")
    public String myCourse(@RequestParam(value = "kind", defaultValue = "usingCourse") String kind, Model model,
                           @AuthenticationPrincipal CustomUser users) {
        // TODO 오어스 계정에 대한 코딩
        if (taste.getTasteList(users.getUsername() + "/" + users.getAuthorities().toArray()[0] + "/").size() == 0) return "redirect:/taste?required";

        if (kind.equals("usingCourse")) {
            return "user/usingCourse";
        } else if (kind.equals("makeCourse")){
            return "user/makeCourse";
        } else {
            return "redirect:/myCourse";
        }
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
