package com.example.demo.controller.android;

import com.example.demo.domain.ManagerVO;
import com.example.demo.domain.UserVO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/android/*")
@RequiredArgsConstructor
@Log4j2
public class AndroidCommonController {
    private final UserService users;

    @PostMapping("/login")
    public String login(HttpServletRequest request) {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        UserVO vo = users.getByEmail("id");
        if (new BCryptPasswordEncoder().matches(pw, vo.getPw())) {
            JSONObject obj = new JSONObject();
            obj.put("user", vo);
            obj.put("message", true);
            return obj.toJSONString();
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/userJoin")
    public String userJoin(UserVO vo) {
        log.info(vo);

        vo.setUCode(vo.getUCode() + "/U/");
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
        if (users.registerManager(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }
}
