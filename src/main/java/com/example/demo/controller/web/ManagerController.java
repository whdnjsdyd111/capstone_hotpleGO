package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.*;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final ReservationService reservation;
    private final ReviewService review;

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

    @GetMapping("/close")
    public String closeSetting() {
        return "manager/close";
    }

    @GetMapping("/open")
    public String openSetting() {
        // TODO
        return "manager/open";
    }

    @GetMapping("/menus/{htId}")
    public String menuManagement(Model model, @PathVariable("htId") String htId) {
        List<MenuVO> list = menu.getList(htId);
        // TODO
        if (list == null || list.size() == 0) return "manager/menus";
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<>()).add(l));
        model.addAttribute("menu_map", map);
        return "manager/menus";
    }

    @GetMapping("/myShop")
    public String myShop(Model model, @AuthenticationPrincipal CustomUser manager) {
        // TODO
//        String uCode = manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/";
        model.addAttribute("hotples", hotple.getByUCode("whdnjsdyd2@naver.com/M/"));
        return "manager/myShop";
    }

    @GetMapping("/reviews/{htId}")
    public String review(Model model, @PathVariable("htId") long htId) {
        // TODO
        model.addAttribute("reviews", review.getList(htId));
        return "manager/reviews";
    }

    @GetMapping(value = { "/reviews", "/menus" })
    public String select(Model model, HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        // TODO
//        String uCode = manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/";
        model.addAttribute("hotples", hotple.getByUCode("whdnjsdyd2@naver.com/M/"));
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

    @GetMapping("/setting")
    public String userSetting(Model model) {
        // TODO
        model.addAttribute("user", user.getManager("whdnjsdyd1111@naver.com/M/"));
        return "manager/userSetting";
    }

    @GetMapping("/sales")
    public String sales(Model model) {
        // TODO
        Map<String, List<ReservationAllVO>> map = reservation.getSales(5L);
        log.info(map);
        model.addAttribute("sales", map);
        return "manager/sales";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        // TODO
        Map<String, List<ReservationAllVO>> map = reservation.getList(5L);
        List<ReviewVO> list = review.getList(5L);
        Map<String, ReviewVO> reviewMap = new HashMap<>();
        list.forEach(l -> reviewMap.computeIfAbsent(l.getRiCode(), k -> l));
        model.addAttribute("reservations", map);
        model.addAttribute("review", reviewMap);
        return "manager/orders";
    }

    @GetMapping(value = { "/", "/main" })
    public String main(Model model, @AuthenticationPrincipal CustomUser manager) {
        // TODO

        if (manager == null) {
            // 업체등록이나 회원가입 관련 공지사항,
            return "manager/mainLogout";
        } else {
            // 공지사항 => 최근꺼 5개 o, 리뷰, 예약 손님 현황, 예약에 대한 메뉴 현황, 매출현황 일주일, 예약으로 방문한 인원 현황
            model.addAttribute("events", event.getCurrentFive());
            model.addAttribute("reviews", review.getCurrentFive());
            model.addAttribute("reservationInfos", reservation.getCurFive(5L));
            model.addAttribute("reservationAll", reservation.getAllCurFive(5L));
            model.addAttribute("reservations", reservation.getList(5L));
            model.addAttribute("sales", reservation.getSales(5L));
            return "manager/main";
        }
    }

}
