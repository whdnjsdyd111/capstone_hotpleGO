package com.example.demo.controller.web;

import com.example.demo.domain.UserVO;
import com.example.demo.service.implement.UserImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CommonController {
    private final UserImpl user;
    private final HttpSession session;

    @GetMapping("/accessError")
    public String accessDenied(Authentication auth, Model model) {
        log.info("access Denied: " + auth);

        model.addAttribute("msg", "Access Denied : " + auth.getAuthorities().toArray()[0] + " 권한은 접근 불가");

        return "accessError";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutGET() {
        log.info("custom logout");

        return "index";
    }

    @PostMapping("/logout")
    public String logoutPost() {
        log.info("post custom logout");

        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/oauth2_subscription")
    public String subscription(Model model) {
        if (session.getAttribute("OAuthUser") == null) {
            return "login";
        }
        model.addAttribute("user", (UserVO) session.getAttribute("OAuthUser"));
        session.removeAttribute("OAuthUser");
        return "subscription";
    }

    @PostMapping("/subscription")
    public String subscription(UserVO vo, @RequestParam("repeatPw") String repeatPw, Model model) {
        if (!vo.getPw().equals(repeatPw)) {
            model.addAttribute("msg", "Passwords do not match.");
            return "register";
        } else if (user.getByEmail(vo.getUcode()) != null) {
            model.addAttribute("msg", "This email has already been signed up.");
            return "register";
        }
        model.addAttribute("user", vo);
        return "subscription";
    }

    @PostMapping("/registerComplete")
    public String registerComplete(UserVO vo, @RequestParam("birthday") String birth, Model model) {
        try {
            vo.setBirth(new SimpleDateFormat("yyyy-MM-dd").parse(birth));
        } catch (ParseException e) {
            model.addAttribute("user", vo);
            return "subscription";
        }

        String socialType = session.getAttribute("registrationId").toString();

        if (socialType == null) {
            vo.setUcode(vo.getUcode() + "/U/");
        } else {
            vo.setUcode(vo.getUcode() + "/U/" + socialType);
        }

        log.info(vo);
        return user.register(vo) ? "redirect:login" : "error_page";
    }
}
