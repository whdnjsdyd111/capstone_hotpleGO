package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.*;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
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
    private final GuideService guide;
    private final UserService users;

    @GetMapping({"/", "/main"})
    public String index(@AuthenticationPrincipal CustomUser user, Model model) {
        String urlOnOff = user == null ? "off" : "on";
        try {
            HttpURLConnection conn = null;
            URL url = new URL("http://127.0.0.1:5000/" + urlOnOff); // 액세스 토큰 받을 주소 입력

            String mbti = "[{mbti: ";
            if (user != null) {
                mbti = this.user.getMbti(user.user.getUCode());
                if (mbti == null) return "redirect:/mbti";  // mbti 없으면 mbti 설정 페이지로 리다이렉트
            }
            mbti += "}]";

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");  // post 방식으로 요청

            // 헤더 설정
            conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정
            // 키 설정
            String hotples_json = new Gson().toJson(hotple.getByGeoAndArea(35.8992601, 128.6215081, 3));
            String courses_json = new Gson().toJson(course.getCourses());
            String courseInfos_json = new Gson().toJson(course.getCourseInfos());

            // JSON 화한 키 값들 전달하기
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write("{" + hotples_json + "," + courses_json + "pp," + courseInfos_json + ",oo" + mbti + "}");   //json 객체로 1차적으로 다듬어서 보냄.
            bw.flush();
            bw.close();

            int responseCode = conn.getResponseCode();
            log.info("응답 코드 : " + responseCode);

            if (responseCode == 200) {  // 성공
                log.info("메인 받기 성공");
                // 버퍼 리더로 반환 값 얻기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                // 차례로 읽기
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                log.info("" + sb.toString());

                String str = sb.toString();
                log.info(str);
            } else {
                log.info(conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/aroundHotple";
    }

    @GetMapping("/alliance")
    public String alliance() {
        return "user/alliance";
    }

    @GetMapping("/setting")
    public String setting(Model model, @AuthenticationPrincipal CustomUser user) {
        GuideApplyVO guideApplyVO = guide.checkGuide(user.user.getUCode());
        GuideVO guideVO = guide.yourGuide(user.user.getUCode());
        model.addAttribute("user", user.user);
        model.addAttribute("guide", guideApplyVO);
        model.addAttribute("yourGuide", guideVO);
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
    public String dibs(@AuthenticationPrincipal CustomUser user, Model model) {
        String uCode = user.user.getUCode();

        List<HotpleVO> pickHotpleList = users.getPickHotpleList(uCode);
        List<CourseVO> pickCourseList = users.getPickCourseList(uCode);
        model.addAttribute("result", pickHotpleList);
        model.addAttribute("result1", pickCourseList);
        model.addAttribute("courseInfos", course.getDibsCourseInfo(uCode));
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

    @GetMapping("/forgotPw")
    public String forgotPw() {
        return "user/forgotPassword";
    }

    @PostMapping("/checkPw")
    public String checkPw(Model model, HttpServletRequest request) {
        String uCode = request.getParameter("username");
        log.info(uCode);

        UserVO vo = user.getByEmail(uCode);
        log.info(vo);
        if (vo == null) {
            return "redirect:/user/forgotPw";
        } else {

            model.addAttribute("uCode", vo.getUCode());
            return "user/checkPw";
        }
    }
}
