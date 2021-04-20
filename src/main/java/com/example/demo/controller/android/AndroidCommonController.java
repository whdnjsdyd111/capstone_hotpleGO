package com.example.demo.controller.android;

import com.example.demo.domain.ManagerVO;
import com.example.demo.domain.UserVO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/android/*")
@RequiredArgsConstructor
@Log4j2
public class AndroidCommonController {
    private final PasswordEncoder passwordEncoder;
    private final UserService users;

    // 유저 정보

    @PostMapping("/login")
    public String login(HttpServletRequest request) {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        UserVO vo = users.getByEmail("id");
        if (vo == null) {
            return "{message: '아이디 또는 비밀번호가 틀렸습니다.'}";
        } else if (new BCryptPasswordEncoder().matches(pw, vo.getPw())) {
            JSONObject obj = new JSONObject();
            obj.put("user", vo);
            obj.put("message", true);
            return obj.toJSONString();
        } else {
            return "{message: '아이디 또는 비밀번호가 틀렸습니다.'}";
        }
    }

    @PostMapping("/check_email")
    public String checkEmail(HttpServletRequest request) {
        String email = request.getParameter("email");
        UserVO vo = users.getByEmail(email);

        if (vo == null) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/userJoin")
    public String userJoin(UserVO vo, HttpServletRequest request) {
        Date birth = null;
        try {
            birth = new SimpleDateFormat("yyMMdd").parse(request.getParameter("birth_str"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info(vo);

        vo.setUCode(vo.getUCode() + "/U/");
        vo.setPw(passwordEncoder.encode(vo.getPw()));
        vo.setBirth(birth);
        if (users.register(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/managerJoin")
    public String managerJoin(ManagerVO vo) {
        log.info(vo);

        vo.setUCode(vo.getUCode() + "/M/");
        vo.setPw(passwordEncoder.encode(vo.getPw()));
        if (users.registerManager(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    // 코스 정보



    // 핫플 공유




}
