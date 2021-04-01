package com.example.demo.controller.web;

import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.MenuVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {
    private final UserService user;
    private final TasteService taste;
    private final HotpleService hotple;
    private final MenuService menu;
    private final ReviewService review;

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

    @GetMapping("/around")
    public String test2() {
        return "user/aroundHotple";
    }

    @GetMapping("/hotple/{htId}")
    public String hotple(@PathVariable("htId") String htId, Model model) {
        HotpleVO hotple = this.hotple.getId(htId);
        model.addAttribute("hotple", hotple);
        List<MenuVO> list = menu.getList(htId);
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<>()).add(l));
        model.addAttribute("menu_map", map);
        model.addAttribute("reviews", review.getList(hotple.getHtId()));
        return "user/shopDetail";
    }
}
