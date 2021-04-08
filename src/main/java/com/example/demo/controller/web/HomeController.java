package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.MenuVO;
import com.example.demo.domain.ReservationAllVO;
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
//        if (taste.getTasteList(users.getUsername() + "/" + users.getAuthorities().toArray()[0] + "/").size() == 0) return "redirect:/taste?required";
        model.addAttribute("kind", kind);
        if (kind.equals("usingCourse")) {
            model.addAttribute("usingCourse", course.getUsingCourse("whdnjsdyd111@naver.com/A/"));
            model.addAttribute("usingCourseInfos", course.getUsingCourseInfo("whdnjsdyd111@naver.com/A/"));
            return "user/usingCourse";
        } else if (kind.equals("myCourse")) {
            model.addAttribute("courses", course.getMakingCourse("whdnjsdyd111@naver.com/A/"));
            model.addAttribute("courseInfos", course.getMakingCourseInfo("whdnjsdyd111@naver.com/A/"));
            return "user/myCourse";
        } else if (kind.equals("usedCourse")) {
            model.addAttribute("courses", course.getHistoryCourse("whdnjsdyd111@naver.com/A/"));
            model.addAttribute("courseInfos", course.getHistoryCourseInfo("whdnjsdyd111@naver.com/A/"));
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
    public String hotple(@PathVariable("htId") String htId, Model model) {
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
        model.addAttribute("openInfos", openInfo.getList(hotple.getHtId()));
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
    public String reservation(Model model) {
        // TODO
        Map<String, List<ReservationAllVO>> map = reservation.getList("whdnjsdyd111@naver.com/A/");
        model.addAttribute("hotples", reservation.getHotples("whdnjsdyd111@naver.com/A/"));
        model.addAttribute("reviews", review.getListByUser("whdnjsdyd111@naver.com/A/"));
        model.addAttribute("reservations", map);
        return "user/reservation";
    }
}
