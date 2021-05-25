package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.EventVO;
import com.example.demo.domain.GuideVO;
import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.web.AllianceVO;
import com.example.demo.domain.web.FeedbackVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.example.demo.service.web.AllianceService;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/admin/rest/*")
@RequiredArgsConstructor
@Log4j2
public class AdminRestController {
    private final AllianceService alliance;
    private final ImageAttachService imageAttach;
    private final EventService event;
    private final GuideService guide;
    private final ReportService report;
    private final BoardService board;
    private final CommentService comment;

    @PostMapping(value = "/changeAlc")
    public ResponseEntity<String> changeAlc(@RequestBody AllianceVO vo) {
        return alliance.change(UriEncoder.decode(vo.getAlcCode())) ? new ResponseEntity<>("성공적으로 처리하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("다시 시도해주십시요.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteAlc")
    public ResponseEntity<String> deleteAlc(@RequestBody AllianceVO vo) {
        return alliance.remove(UriEncoder.decode(vo.getAlcCode())) ? new ResponseEntity<>("성공적으로 삭제하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("다시 시도해주십시요.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/changeFeed")
    public ResponseEntity<String> changeFeed(@RequestBody FeedbackVO vo) {
        return alliance.change(UriEncoder.decode(vo.getFeedCode())) ? new ResponseEntity<>("성공적으로 처리하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("다시 시도해주십시요.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteFeed")
    public ResponseEntity<String> deleteFeed(@RequestBody FeedbackVO vo) {
        return alliance.remove(UriEncoder.decode(vo.getFeedCode())) ? new ResponseEntity<>("성공적으로 삭제하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("다시 시도해주십시요.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/ck_upload")
    public Map<String, Object> upload(@RequestParam Map<String, Object> map, MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("upload");
        ImageAttachVO vo = new ImageAttachVO();
        String url = vo.upload(file);
        map.put("uploaded", 1);
        map.put("url", url);
        map.put("fileName", vo.getFileName());
        imageAttach.upload(vo);
        return map;
    }

    @PostMapping("/registerEvent")
    public ResponseEntity<String> registerEvent(@RequestBody EventVO vo, @AuthenticationPrincipal CustomUser admin) {
        vo.setUCode(admin.getUsername() + "/" + admin.getAuthorities().toArray()[0] + "/");
        boolean bol = false;
        String str = null;
        if (vo.getEveStart() != null && vo.getEveExpi() != null) {
            bol = event.registerEvent(vo);
            str = "이벤트 등록 완료하였습니다.";
        } else {
            bol = event.registerAnnounce(vo);
            str = "공지사항 등록 완료하였습니다.";
        }
        return bol ? new ResponseEntity<>(str, HttpStatus.OK) :
                new ResponseEntity<>("등록 실패하였습니다.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateEvent")
    public ResponseEntity<String> updateEvent(@RequestBody EventVO vo, @AuthenticationPrincipal CustomUser admin) {
        boolean bol = false;
        if (vo.getEveStart() != null && vo.getEveExpi() != null) {
            if (vo.getEveCode().charAt(16) == 'A') {
                bol = event.changeAtoE(vo);
            } else {
                bol = event.change(vo);
            }
        } else {
            if (vo.getEveCode().charAt(16) == 'E') {
                bol = event.changeEtoA(vo);
            } else {
                bol = event.change(vo);
            }
        }
        return bol ? new ResponseEntity<>("수정 완료 하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("수정 실패하였습니다.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteEvent")
    public ResponseEntity<String> deleteEvent(@RequestBody EventVO vo) {
        return event.remove(UriEncoder.decode(vo.getEveCode())) ? new ResponseEntity<>("삭제 완료하였습니다.", HttpStatus.OK)
                : new ResponseEntity<>("삭제 실패하였습니다. 다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/confirmGuide")
    @ResponseBody
    public ResponseEntity<String> confirmGuide(HttpServletRequest request, String uCode) {
        GuideVO vo = new GuideVO();
        vo.setUCode(request.getParameter("uCode"));
        log.info(vo);
        boolean isInserted = guide.confirmGuide(vo);
        try {
            if (isInserted == true) {
                log.info("성공");
                guide.removeGuideApply(uCode);
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("등록 완료", HttpStatus.OK);
    }

    @PostMapping("/deleteGuideApply")
    public ResponseEntity<String> removeGuideApply(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        boolean isDeleted = guide.removeGuideApply(uCode);
        try {
            if (isDeleted == true) {
                log.info("성공");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deleteReport")
    public ResponseEntity<String> deleteReport(HttpServletRequest request) {
        String repCode = request.getParameter("repCode");
        boolean isDeleted = report.deleteReport(repCode);
        log.info(repCode);
        try {
            if (isDeleted == true) {
                log.info("삭제 완료");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    @PostMapping("/deleteGuide")
    public ResponseEntity<String> deleteGuide(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        boolean isDeleted = guide.deleteGuide(uCode);
        log.info(uCode);
        try {
            if (isDeleted == true) {
                log.info("삭제 완료");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    @PostMapping("/deleteContent")
    public ResponseEntity<String> deleteContent(HttpServletRequest request) {
        String bdCode = request.getParameter("bdCode");
        boolean isUpdate = board.boardType(bdCode);
        log.info(bdCode);
        try {
            if (isUpdate == true) {
                log.info("수정 완료");
                return new ResponseEntity<>("수정 성공", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.warn("에러 발생");
            return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    @PostMapping("/deleteCommContent")
    public ResponseEntity<String> deleteCommContent(HttpServletRequest request) {
        String comCode = request.getParameter("comCode");
        boolean isChange = comment.commentType(comCode);
        log.info(comCode);
        try {
            if (isChange == true) {
                log.info("수정 완료");
                return new ResponseEntity<>("수정 성공", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.warn("에러 발생");
            return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("수정완료", HttpStatus.OK);
    }

    @PostMapping("/deleteReContent")
    public ResponseEntity<String> deleteReContent(HttpServletRequest request) {
        String bdCode = request.getParameter("bdCode");
        boolean isUpdate = board.boardReType(bdCode);
        log.info(bdCode);
        try {
            if (isUpdate == true) {
                log.info("수정 완료");
                return new ResponseEntity<>("수정 성공", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.warn("에러 발생");
            return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    @PostMapping("/deleteReCommContent")
    public ResponseEntity<String> deleteReCommContent(HttpServletRequest request) {
        String comCode = request.getParameter("comCode");
        boolean isChange = comment.commentReType(comCode);
        log.info(comCode);
        try {
            if (isChange == true) {
                log.info("수정 완료");
                return new ResponseEntity<>("수정 성공", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.warn("에러 발생");
            return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("수정완료", HttpStatus.OK);
    }
}
