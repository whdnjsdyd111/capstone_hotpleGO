package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.*;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final OpenInfoService openInfo;

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
    public String login(@AuthenticationPrincipal CustomUser manager) {
        if (manager != null) return "redirect:/manager/main";
        return "manager/login";
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) new SecurityContextLogoutHandler().logout(request, response, auth);

        log.info("custom logout");
        return "redirect:/manager/main";
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
    public String openSetting(Model model, @AuthenticationPrincipal CustomUser manager) {
        model.addAttribute("opens", openInfo.getListByManager(manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/"));
        return "manager/open";
    }

    @GetMapping("/menus")
    public String menuManagement(Model model, @AuthenticationPrincipal CustomUser manager) {
        List<MenuVO> list = menu.getListByUser(manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/");
        if (list == null || list.size() == 0) return "manager/menus";
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<>()).add(l));
        model.addAttribute("menu_map", map);
        return "manager/menus";
    }

    @GetMapping("/myShop")
    public String myShop(Model model, @AuthenticationPrincipal CustomUser manager) {
        model.addAttribute("hotples", hotple.getByUCode(manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/"));
        return "manager/myShop";
    }

    @GetMapping("/reviews")
    public String review(Model model, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/";
        model.addAttribute("reviews", review.getListByManager(uCode));
        model.addAttribute("ratings", review.getRatings(uCode));
        return "manager/reviews";
    }

    @GetMapping("/announce")
    public String announceList(@RequestParam(value = "sort", defaultValue = "event") String sort, Criteria cri, Model model) {
        if (sort.equals("event")) {
            model.addAttribute("events", event.getEventList(cri));
        } else if (sort.equals("announce")) {
            model.addAttribute("events", event.getAnnounceList(cri));
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
    public String userSetting(Model model, @AuthenticationPrincipal CustomUser manager) {
        model.addAttribute("user", user.getManager(manager.user.getUCode()));
        return "manager/userSetting";
    }

    @GetMapping("/sales")
    public String sales(Model model, @AuthenticationPrincipal CustomUser manager) {
        Map<String, List<ReservationAllVO>> map =
                reservation.getSales(manager.user.getUCode());
        log.info(map);
        model.addAttribute("sales", map);
        return "manager/sales";
    }

    @GetMapping("/orders")
    public String orders(Model model, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.user.getUCode();
        Map<String, List<ReservationAllVO>> map = reservation.getListByManager(uCode);
        List<ReviewVO> list = review.getListByManager(uCode);
        Map<String, ReviewVO> reviewMap = new HashMap<>();
        list.forEach(l -> reviewMap.computeIfAbsent(l.getRiCode(), k -> l));
        model.addAttribute("reservations", map);
        model.addAttribute("review", reviewMap);
        return "manager/orders";
    }

    @GetMapping(value = { "/", "/main" })
    public String main(Model model, @AuthenticationPrincipal CustomUser manager) {
        if (manager == null) {
            // 업체등록이나 회원가입 관련 공지사항,
            return "manager/mainLogout";
        } else {
            String uCode = manager.user.getUCode();
            if (!uCode.split("/")[1].equals("M")) return "manager/mainLogout";
            // 공지사항 => 최근꺼 5개 o, 리뷰, 예약 손님 현황, 예약에 대한 메뉴 현황, 매출현황 일주일, 예약으로 방문한 인원 현황
            model.addAttribute("events", event.getCurrentFive());
            model.addAttribute("reviews", review.getCurrentFive());
            model.addAttribute("reservationInfos", reservation.getCurFive(uCode));
            model.addAttribute("reservationAll", reservation.getAllCurFive(uCode));
            model.addAttribute("reservations", reservation.getListByManager(uCode));
            model.addAttribute("sales", reservation.getSales(uCode));
            return "manager/main";
        }
    }

}
