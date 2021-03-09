package com.example.demo.controller.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.domain.web.ChatLogVO;
import com.example.demo.security.CustomUser;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.implement.UserImpl;
import com.example.demo.service.web.implement.AllianceImpl;
import com.example.demo.service.web.implement.ChatLogImpl;
import com.example.demo.service.web.implement.FeedbackImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/*")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private final AllianceImpl alliance;
    private final ChatLogImpl chatLog;
    private final UserImpl user;
    private final FeedbackImpl feedback;

    @GetMapping("/main")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "admin/statistics";
    }

    @GetMapping("/members")
    public String memberManagement() {
        return "admin/index";
    }

    @GetMapping("/contents")
    public String contentsManagement() {
        return "admin/index";
    }

    @GetMapping("/chattingRoom")
    public String chattingRoom(Model model, @AuthenticationPrincipal CustomUser admin) {
        model.addAttribute("chatLog", chatLog.getListChatLog());
        model.addAttribute("admin", user.getAdmin(admin.getUsername() + "/"
                + admin.getAuthorities().toArray()[0] + "/"));
        return "admin/chattingRoom";
    }

    @GetMapping("/alliances")
    public String alliances(@RequestParam(value = "sort", defaultValue = "nonProcessed") String sort, Model model) {
        if (sort.equals("nonProcessed")) {
            model.addAttribute("NAlliances", alliance.getListN());
        } else if (sort.equals("processed")) {
            model.addAttribute("YAlliances", alliance.getListY());
        } else {
            return "redirect:/admin/alliances";
        }

        return "admin/alliances";
    }

    @GetMapping("/feedback")
    public String feedback(@RequestParam(value = "sort", defaultValue = "nonProcessed") String sort, Model model) {
        if (sort.equals("nonProcessed")) {
            model.addAttribute("NFeedback", feedback.getListN());
        } else if (sort.equals("processed")) {
            model.addAttribute("YFeedback", feedback.getListY());
        } else {
            return "redirect:/admin/feedback";
        }

        return "admin/feedback";
    }

    @GetMapping("/reports")
    public String reports() {
        return "admin/index";
    }
}
