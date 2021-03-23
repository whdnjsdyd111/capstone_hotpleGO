package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.EventVO;
import com.example.demo.domain.MenuVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.EventService;
import com.example.demo.service.HotpleService;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import com.example.demo.domain.ManagerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private final EventService event;

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

    @GetMapping("/menus/{htId}")
    public String menuManagement(Model model, @PathVariable("htId") String htId) {
        List<MenuVO> list = menu.getList(htId);
        // TODO
        if (list == null || list.size() == 0) return "redirect:/manager/menus";
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<>()).add(l));
        model.addAttribute("menu_map", map);
        return "manager/menus";
    }

    @GetMapping("/myShop")
    public String myShop(Model model, @AuthenticationPrincipal CustomUser manager) {
//        String uCode = manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/";
        model.addAttribute("hotples", hotple.getByUCode("whdnjsdyd1111@naver.com/M/"));
        return "manager/myShop";
    }

    @GetMapping("/reviews/{htId}")
    public String review(Model model, @PathVariable("htId") String htId) {
        return "manager/reviews";
    }

    @GetMapping(value = { "/reviews", "/menus" })
    public String select(Model model, HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
//        String uCode = manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/";
        model.addAttribute("hotples", hotple.getByUCode("whdnjsdyd1111@naver.com/M/"));
        model.addAttribute("url", request.getRequestURI().split("/")[2]);
        return "manager/selectComp";
    }

    @GetMapping("/announce")
    public String announceList(@RequestParam(value = "sort", defaultValue = "event") String sort, Model model) {
        if (sort.equals("event")) {
            model.addAttribute("events", event.getEventList());
        } else if (sort.equals("announce")) {
            model.addAttribute("events", event.getAnnounceList());
        } else {
            return "redirect:/manager/announceList";
        }
        model.addAttribute("sort", sort);
        return "manager/announceList";
    }

    @GetMapping("/announce/{code}")
    public String announceContent(Model model, @PathVariable("code") String str) {
        String code = HotpleAPI.strToCode(str);
        if (code == null) return "redirect:/manager/announce";

        EventVO vo = event.getEvent(code);
        if (vo == null) {
            return "redirect:/manager/announce";
        }
        model.addAttribute("event", vo);
        return "manager/announce";
    }
    @GetMapping("/test")
    public String test() {
        return "manager/test";
    }
}
