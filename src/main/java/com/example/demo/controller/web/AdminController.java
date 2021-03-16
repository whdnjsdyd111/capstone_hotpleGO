package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.EventVO;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import com.example.demo.service.web.AllianceService;
import com.example.demo.service.web.FeedbackService;
import com.example.demo.security.CustomUser;
import com.example.demo.service.web.ChatLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/*")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private final AllianceService alliance;
    private final ChatLogService chatLog;
    private final UserService user;
    private final FeedbackService feedback;
    private final EventService event;

    @GetMapping("/main")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/login")
    public String loginAdmin() {
        return "admin/login";
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

    @GetMapping("/announceWrite")
    public String announce() {
        return "admin/announceWrite";
    }

    @GetMapping("/announce/{code}")
    public String announceContent(@PathVariable("code") String str, Model model) {
        String code = HotpleAPI.strToCode(str);
        if (code == null) return "redirect:/admin/announce";

        EventVO vo = event.getEvent(code);
        if (vo == null) {
            return "redirect:/admin/announce";
        }
        model.addAttribute("event", vo);
        return "admin/announceContent";
    }

    @GetMapping("/announce")
    public String announceList(@RequestParam(value = "sort", defaultValue = "event") String sort, Model model) {
        if (sort.equals("event")) {
            model.addAttribute("events", event.getEventList());
        } else if (sort.equals("announce")) {
            model.addAttribute("events", event.getAnnounceList());
        } else {
            return "redirect:/admin/announce";
        }
        model.addAttribute("sort", sort);
        return "admin/announce";
    }

    @GetMapping("/announceUpdate/{code}")
    public String announceUpdate(@PathVariable("code") String str, Model model) {
        String code = HotpleAPI.strToCode(str);
        if (code == null) return "redirect:/admin/announce";

        EventVO vo = event.getEvent(code);
        if (vo == null) {
            return "redirect:/admin/announce";
        }
        model.addAttribute("event", vo);
        return "admin/announceWrite";
    }

    @GetMapping("/reports")
    public String reports() {
        return "admin/index";
    }
}
