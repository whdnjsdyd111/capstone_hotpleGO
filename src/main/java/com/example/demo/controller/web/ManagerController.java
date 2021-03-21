package com.example.demo.controller.web;

import com.example.demo.domain.MenuVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.HotpleService;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import com.example.demo.domain.ManagerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager/*")
@RequiredArgsConstructor
@Log4j2
public class ManagerController {
    private final UserService user;
    private final HotpleService hotple;
    private final MenuService menu;

    @GetMapping("/register")
    public String registerManager() {
        return "manager/register";
    }

    @PostMapping("/register")
    public String registerManager(ManagerVO vo, @RequestParam("birthday") String birth, Model model) {
        try {
            vo.setBirth(new SimpleDateFormat("yyyy-MM-dd").parse(birth));
        } catch (ParseException e) {
            model.addAttribute("manager", vo);
            return "manager/register";
        }

        log.info(vo);
        vo.setUCode(vo.getUCode() + "/M/");

        return user.registerManager(vo) ? "redirect:/manager/login" : "error_page";
    }

    @GetMapping("/login")
    public String login() {
        return "manager/login";
    }

    @GetMapping("/enrollment")
    public String enrollment() {
        return "manager/enrollment";
    }

    @GetMapping("/stop")
    public String temporaryStop() {
        return "manager/stop";
    }

    @GetMapping("/menus")
    public String menuManagement(Model model) {
        List<MenuVO> list = menu.getList("5");
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> {
            map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<MenuVO>()).add(l);
        });
        model.addAttribute("menu_map", map);
        return "manager/menus";
    }

    @GetMapping("/myShop")
    public String myShop(Model model, @AuthenticationPrincipal CustomUser manager) {
//        String uCode = manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/";
        model.addAttribute("hotples", hotple.getByUCode("whdnjsdyd1111@naver.com/M/"));
        return "manager/myShop";
    }
    @GetMapping("/test2")
    public String test2() {
        return "manager/test2";
    }
}
