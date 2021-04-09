package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.*;
import com.example.demo.domain.web.AllianceVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.example.demo.service.web.AllianceService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/*")
@RequiredArgsConstructor
@Log4j2
public class HomeRestController {
    private final AllianceService alliance;
    private final UserService user;
    private final TasteService taste;
    private final HotpleService hotple;
    private final ReviewService review;
    private final CourseService course;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/alliance")
    public ResponseEntity<String> allianceRegister(@RequestBody AllianceVO vo) {
        if (vo.getName().isEmpty() || vo.getEmail().isEmpty() || vo.getPhone().isEmpty() || vo.getContent().isEmpty()) {
            return new ResponseEntity<>("모두 빠짐없이 작성해 주십시오.", HttpStatus.BAD_REQUEST);
        }
        return alliance.register(vo) ? new ResponseEntity<>("제휴 감사합니다.", HttpStatus.OK)
                : new ResponseEntity<>("다시 시도 해주십시오.", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/setting-nick")
    public ResponseEntity<String> settingNick(@RequestBody UserVO vo) {
        // TODO
        if (user.updateNick(vo.getNick(), "whdnjsdyd1112@naver.com/U/GO")) {
            return new ResponseEntity<>("닉네임 변경 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setting-password")
    public ResponseEntity<String> settingPassword(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        String pw = request.getParameter("password");
        String new_pw = request.getParameter("new_password");
        String code = user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/";
        if (new BCryptPasswordEncoder().matches(pw, this.user.getPw(code))) {
            if (this.user.updatePw(passwordEncoder.encode(new_pw), code)) {
                return new ResponseEntity<>("비밀번호 변경 완료하였습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("변경 실패하였습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/save-mbti")
    public ResponseEntity<String> saveMBTI(HttpServletRequest request) {
        // TODO
        log.info(request.getParameter("mbti"));
        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/save-taste")
    @ResponseBody
    public ResponseEntity<String> saveTaste(@RequestParam(value = "tastes[]") List<Integer> tastes,
                                            @AuthenticationPrincipal CustomUser user) {
        String code = user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/";
        taste.reset(code);
        if (taste.registerAll(code, tastes)) {
            return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setGeo")
    public ResponseEntity<List<HotpleVO>> setGeo(HttpServletRequest request) {
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));
        return new ResponseEntity<>(hotple.getByKeywordGeo(request.getParameter("keyword"), lat, lng), HttpStatus.OK);
    }

    @PostMapping("/submit-review")
    public ResponseEntity<String> submitReview(@RequestBody ReviewVO vo) {
        // TODO
        vo.setUCode("whdnjsdyd111@naver.com/A/");
        vo.setRiCode(HotpleAPI.strToCode(vo.getRiCode()));
        if (review.registerReview(vo)) {
            return new ResponseEntity<>("댓글 작성 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update-review")
    public ResponseEntity<String> updateReview(@RequestBody ReviewVO vo) {
        vo.setRiCode(HotpleAPI.strToCode(vo.getRiCode()));
        if (review.updateReview(vo)) {
            return new ResponseEntity<>("댓글 수정하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/custom-course")
    public ResponseEntity<String> customCourse(@RequestBody CourseVO vo) {
        if (course.register(vo)) {
            log.info(vo.getCsCode());
            return new ResponseEntity<>(vo.getCsCode(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }
}
