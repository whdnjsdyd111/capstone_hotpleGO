package com.example.demo.controller.web;

import org.python.util.PythonInterpreter;
import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.CourseWithMbtiVO;
import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.MenuVO;
import com.example.demo.domain.ReservationAllVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Select;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@Controller
@RequiredArgsConstructor
@Log4j2
public class HomeController {
    private final UserService user;
    private final TasteService taste;
    private final HotpleService hotple;
    private final MenuService menu;
    private final ReviewService review;
    private final OpenInfoService openInfo;
    private final CourseService course;
    private final ReservationService reservation;

    @GetMapping({ "/", "/main"})
    public String index() {
        return "user/aroundHotple";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "user/alliance";
    }

    @GetMapping("/setting")
    public String setting(Model model, @AuthenticationPrincipal CustomUser user) {
        model.addAttribute("user", user.user);
        return "user/userSetting";
    }

    @GetMapping("/mbti")
    public String mbti(Model model, @AuthenticationPrincipal CustomUser user) {
        model.addAttribute("mbti", this.user.getMbti(user.user.getUCode()));
        return "user/mbti";
    }



    @GetMapping("/taste")
    public String taste(@RequestParam(value = "required", required = false) String required, Model model,
                        @AuthenticationPrincipal CustomUser user) {
        if (required != null) {
            model.addAttribute("msg", "취향 선택 후 코스를 이용해 주십시오.");
        }
        model.addAttribute("tastes", taste.getTasteList(user.user.getUCode()));
        return "user/taste";
    }

    @GetMapping("/myCourse")
    public String myCourse(@RequestParam(value = "kind", defaultValue = "usingCourse") String kind, Model model,
                           @AuthenticationPrincipal CustomUser user) {
        // TODO 오어스 계정에 대한 코딩
        String uCode = user.user.getUCode();
        if (taste.getTasteList(uCode).size() == 0) return "redirect:/taste?required";
        model.addAttribute("kind", kind);
        if (kind.equals("usingCourse")) {
            model.addAttribute("usingCourse", course.getUsingCourse(uCode));
            model.addAttribute("usingCourseInfos", course.getUsingCourseInfo(uCode));
            return "user/usingCourse";
        } else if (kind.equals("myCourse")) {
            model.addAttribute("courses", course.getMakingCourse(uCode));
            model.addAttribute("courseInfos", course.getMakingCourseInfo(uCode));
            return "user/myCourse";
        } else if (kind.equals("usedCourse")) {
            model.addAttribute("courses", course.getHistoryCourse(uCode));
            model.addAttribute("courseInfos", course.getHistoryCourseInfo(uCode));
            return "user/courseHistory";
        } else {
            return "redirect:/myCourse";
        }
    }

    @GetMapping("/courseDetail/{csCode}")
    public String courseDetail(@PathVariable("csCode") String csCode, Model model) {
        String code = HotpleAPI.strToCode(csCode);
        log.info(code);
        model.addAttribute("course", course.getCourseDetail(code));
        model.addAttribute("courseInfos", course.getCourseInfoDetail(code));
        return "user/courseDetail";
    }

    @GetMapping("/dibs")
    public String dibs(@AuthenticationPrincipal CustomUser users) {
        return "user/dibs";
    }

    @GetMapping("/hotple/{htId}")
    public String hotple(@PathVariable("htId") String htId, Model model, @AuthenticationPrincipal CustomUser user) {
        HotpleVO hotple = this.hotple.getId(htId);
        model.addAttribute("hotple", hotple);
        List<MenuVO> list = menu.getList(htId);
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<>()).add(l));
        model.addAttribute("menu_map", map);
        model.addAttribute("max", list.stream().mapToLong(MenuVO::getMePrice).max().orElse(0));
        model.addAttribute("min", list.stream().mapToLong(MenuVO::getMePrice).min().orElse(0));
        model.addAttribute("avg", list.stream().mapToLong(MenuVO::getMePrice).average().orElse(0));
        model.addAttribute("reviews", review.getList(hotple.getHtId()));
        model.addAttribute("ratings", review.getRatingsHotple(Long.parseLong(htId)));
        model.addAttribute("openInfos", openInfo.getList(hotple.getHtId()));

        if (user != null) {
            String uCode = user.user.getUCode();
            model.addAttribute("courses", course.getAllCourse(uCode));
            model.addAttribute("courseInfos", course.getAllInfo(uCode));
        }

        return "user/shopDetail";
    }


    @GetMapping("/company")
    public String company() {
        return "user/company";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        model.addAttribute("hotples", hotple.getByKeyword(keyword));
        return "user/searchHotple";
    }

    @GetMapping("/reservation")
    public String reservation(Model model, @AuthenticationPrincipal CustomUser user) {
        // TODO
        String uCode = user.user.getUCode();
        Map<String, List<ReservationAllVO>> map = reservation.getList(uCode);
        log.info(map);
        model.addAttribute("hotples", reservation.getHotples(uCode));
        model.addAttribute("reviews", review.getListByUser(uCode));
        model.addAttribute("reservations", map);
        return "user/reservation";
    }

    @GetMapping("/aiCourse")
    public String aiCourse() {
        return "user/makeCourse";
    }

    @GetMapping("/payments")
    public String pay(Model model, @AuthenticationPrincipal CustomUser user) {
        model.addAttribute("email", user.user.getUCode().split("/")[0]);
        model.addAttribute("phone", user.user.getPhone());
        return "user/payment";
    }

}
