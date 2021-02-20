package com.example.demo.controller.web;

import com.example.demo.domain.UserVO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.implement.UserImpl;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Log4j2
public class CommonController {
    @Setter(onMethod_ = @Autowired)
    private UserImpl user;

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

        return "";
    }

    @PostMapping("/logout")
    public String logoutPost() {
        log.info("post custom logout");

        return "";
    }

    @GetMapping("/register")
    public String register() {
        return "/register";
    }

    @PostMapping("/subscription")
    public String subscription(UserVO vo, @RequestParam("repeatPw") String repeatPw, Model model) {
        if (!vo.getPw().equals(repeatPw)) {
            model.addAttribute("msg", "Passwords do not match.");
            return "/register";
        } else if (user.getByEmail(vo.getUcode()) != null) {
            model.addAttribute("msg", "This email has already been signed up.");
            return "/register";
        }
        model.addAttribute("user", vo);
        return "/subscription";
    }

    @PostMapping("/registerComplete")
    public String registerComplete(UserVO vo, @RequestParam("birthday") String birth, Model model) {
        try {
            vo.setBirth(new SimpleDateFormat("yyyy-MM-dd").parse(birth));
        } catch (ParseException e) {
            model.addAttribute("user", vo);
            return "/subscription";
        }
        vo.setUcode(vo.getUcode() + "/U/");

        log.info(vo);
        return user.register(vo) ? "redirect:/login" : "/error_page";
    }
}
