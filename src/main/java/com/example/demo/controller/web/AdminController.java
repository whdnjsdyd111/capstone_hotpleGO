package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.Criteria;
import com.example.demo.domain.EventVO;
import com.example.demo.domain.PageVO;
import com.example.demo.domain.GuideApplyVO;
import com.example.demo.service.EventService;
import com.example.demo.service.GuideService;
import com.example.demo.service.UserService;
import com.example.demo.service.web.*;
import com.example.demo.security.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    private final GuideService guide;
    private final BoardService board;
    private final CommentService comm;



    @GetMapping("/main")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/login")
    public String loginAdmin(@AuthenticationPrincipal CustomUser admin) {
        if (admin != null) return "redirect:/admin/main";
        return "admin/login";
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) new SecurityContextLogoutHandler().logout(request, response, auth);

        log.info("custom logout");
        return "redirect:/admin/login";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "admin/statistics";
    }

    @GetMapping("/members")
    public String memberManagement(Model model) {
        model.addAttribute("result", user.getList());
        return "admin/memberManagement";
    }
    @GetMapping("/contents")
    /*public String contentsManagement(Model model) {
        model.getAttribute("board",board.getBoardList());
        return "admin/contentManagement";*/
        public String contentsManagement(Criteria cri,  Model model) {
            //String str = kind.equals("B") ? ("게시판") : (kind.equals("H") ? "핫플" : "코스");
           // log.info(cri.toString());
            model.addAttribute("result", board.getBoardList(cri));
            //model.addAttribute("pageMaker", new PageVO(cri, board.getTotal(cri)));
            //model.addAttribute("kind", str);
            return "admin/contentManagement";
    }

    /*@GetMapping("/hotple_modal")
    public String companyModal(Model modal, @RequestParam(value = "htId") String htId) {
        modal.addAttribute("hotple", hotple.getHotple(htId));
        return "admin/hotple_modal";
    }*/

    @GetMapping("/content_modal")
    public String contentModal(Model modal, @RequestParam(value = "uCode") String uCode) {
        modal.addAttribute("boards", board.getBoardCodes(uCode));
        modal.addAttribute("comm", comm.getComCodes(uCode));
        modal.addAttribute("reply", comm.getReplyCodes(uCode));

        return "admin/content_modal";
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
    public String announceList(@RequestParam(value = "sort", defaultValue = "event") String sort, Criteria cri, Model model) {
        if (sort.equals("event")) {
            model.addAttribute("events", event.getEventList(cri));
            model.addAttribute("pageMaker", new PageVO(cri, event.getEventTotal(cri)));
        } else if (sort.equals("announce")) {
            model.addAttribute("events", event.getAnnounceList(cri));
            model.addAttribute("pageMaker", new PageVO(cri, event.getAnnounceTotal(cri)));
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
        return "admin/report";
    }

    @GetMapping(value = "/guideApply")
    public String guideList(Model model){
        List<GuideApplyVO> guideApplyList = guide.getGuideList();
        log.info(guideApplyList);
        model.addAttribute("result", guideApplyList);
        return "admin/guideApply";
    }
}
