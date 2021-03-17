package com.example.demo.controller.web;

import com.example.demo.domain.UserVO;
import com.example.demo.service.UserService;
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
    private final UserService user;
    private final HttpSession session;

    @GetMapping("/accessError")
    public String accessDenied(Authentication auth, Model model) {
        log.info("access Denied: " + auth);

        model.addAttribute("msg", "Access Denied : " + auth.getAuthorities().toArray()[0] + " 권한은 접근 불가");

        return "accessError";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "err", required = false) String err, Model model) {
        String msg = "Log In!";
        if (err != null) {
            msg = err.equals("exist") ? "This email has already been signed up in a different way." : "Error";
        }
        model.addAttribute("msg", msg);

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
        } else if (user.getByEmail(vo.getUCode()) != null) {
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

        String socialType = null;
        Object obj = session.getAttribute("registrationId");
        if (obj != null) {
            socialType = obj.toString();
        }

        if (socialType == null) {
            vo.setUCode(vo.getUCode() + "/U/");
        } else {
            vo.setUCode(vo.getUCode() + "/U/" + socialType);
        }
        if(vo.getProfileImg().isEmpty()) vo.setProfileImg(null);

        log.info(vo);
        return user.register(vo) ? "redirect:login" : "error_page";
    }
}
