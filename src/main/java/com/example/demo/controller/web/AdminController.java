package com.example.demo.controller.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.service.web.implement.AllianceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/*")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private final AllianceImpl alliance;

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

    @GetMapping("/chat")
    public String chattingRoom() {
        return "admin/index";
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

    @GetMapping("/reports")
    public String reports() {
        return "admin/index";
    }
}
