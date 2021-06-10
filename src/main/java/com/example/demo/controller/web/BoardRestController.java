package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.ReportVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.ImageAttachService;
import com.example.demo.service.ReportService;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/board/rest/*")
@RequiredArgsConstructor
@Log4j2
public class BoardRestController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final ImageAttachService imageAttach;
    private final ReportService report;

    @PostMapping("/write")
    public ResponseEntity<String> insertBoard(@RequestBody BoardVO boardVO, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boardVO.setUCode(vo.getUCode());
        boolean isInserted = boardService.insertBoard(boardVO);
        try {
            if (isInserted == true) {
                log.info("게시글 등록 성공");
            }

        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러");
        }

        return new ResponseEntity<>("글 등록이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/shareHotple")
    public ResponseEntity<String> insertHotpleShared(@RequestBody BoardVO boardVO, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boardVO.setUCode(vo.getUCode());
        log.info(boardVO);

        boolean isInserted = boardService.insertBoard(boardVO);
        try {
            if (isInserted == true) {
                log.info("게시글 등록 성공");
            }

        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러");
        }
        return new ResponseEntity<>("등록 완료", HttpStatus.OK);
    }

    @PostMapping("/shareCourse")
    public ResponseEntity<String> insertCourseShared(@RequestBody BoardVO boardVO, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boardVO.setUCode(vo.getUCode());
        boolean isInserted = boardService.insertBoard(boardVO);
        try {
            if (isInserted == true) {
                log.info("게시글 등록 성공");
            }

        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러");
        }
        return new ResponseEntity<>("등록 완료", HttpStatus.OK);
    }

    @PostMapping("/view/{bdCode}")
    public ResponseEntity<String> insertComment(@RequestBody CommentVO commentVO, @PathVariable(value = "bdCode") String bdCode, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        commentVO.setUCode(vo.getUCode());
        commentVO.setBdCode(HotpleAPI.strToCode(bdCode));
        commentVO.setReplyCode(commentVO.getReplyCode());
        log.info(commentVO);
        boolean isCommInserted = commentService.commentInsert(commentVO);
        try {
            if (isCommInserted == true) {
                log.info("댓글 등록 성공");
            }
        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러");
        }
        return new ResponseEntity<>("댓글 등록이 완료되었습니다.", HttpStatus.OK);
    }


    @PostMapping("/view/update/{bdCode}")
    public ResponseEntity<String> updateComment(HttpServletRequest request, @PathVariable(value = "bdCode") String bdCode, HttpSession session, @RequestBody CommentVO commentVO) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String uCode = vo.getUCode();
        String comCode = request.getParameter("comCode");
        commentVO.setBdCode(HotpleAPI.strToCode(bdCode));
        commentVO.setReplyCode(commentVO.getReplyCode());
        String comCont = request.getParameter("comCont");
        log.info(commentVO);
        boolean isCommUpdate = commentService.commentUpdate(uCode, comCode, comCont);
        try {
            if (isCommUpdate == true) {
                log.info("댓글 수정 완료");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("댓글 수정 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<String> updateBoard(@RequestBody BoardVO boardVO, Model model, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        log.info(boardVO);
        boolean data = boardService.updateBoard(boardVO, vo.getUCode());
        model.addAttribute("data", data);
        return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/upload")
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

    @PostMapping("/upload_comm")
    public ResponseEntity<String> upload(MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("upload");
        ImageAttachVO vo = new ImageAttachVO();
        String url = vo.upload(file);
        imageAttach.upload(vo);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @PostMapping("/comLike")
    public ResponseEntity<String> comLikeAdd(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boolean com_reco = Boolean.parseBoolean(request.getParameter("check_com_reco"));
        boolean com_non_reco = Boolean.parseBoolean(request.getParameter("check_com_nonReco"));
        String comCode = request.getParameter("comCode");
        log.info(com_reco);
        log.info(com_non_reco);
        log.info(comCode);
        if (com_reco) {
            commentService.deleteComReco(comCode, vo.getUCode());
            commentService.comRecoDown(comCode);
        } else {
            if (com_non_reco) {
                commentService.updateComReco(comCode, vo.getUCode(), 'Y');
                commentService.unComRecoDown(comCode);
            } else {
                commentService.insertComReco(comCode, vo.getUCode(), 'Y');
            }
            commentService.comRecoUp(comCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/unComLike")
    public ResponseEntity<String> unComLikeAdd(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boolean com_reco = Boolean.parseBoolean(request.getParameter("check_com_reco"));
        boolean com_non_reco = Boolean.parseBoolean(request.getParameter("check_com_nonReco"));
        String comCode = request.getParameter("comCode");

        if (com_non_reco) {
            commentService.deleteComReco(comCode, vo.getUCode());
            commentService.unComRecoDown(comCode);
        } else {
            if (com_reco) {
                commentService.updateComReco(comCode, vo.getUCode(), 'N');
                commentService.comRecoDown(comCode);
            } else {
                commentService.insertComReco(comCode, vo.getUCode(), 'N');
            }
            commentService.unComRecoUp(comCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeAdd(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boolean reco = Boolean.parseBoolean(request.getParameter("check_reco"));
        boolean non_reco = Boolean.parseBoolean(request.getParameter("check_nonReco"));
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));

        if (reco) {
            boardService.deleteReco(bdCode, vo.getUCode());
            boardService.recoDown(bdCode);
        } else {
            if (non_reco) {
                boardService.updateReco(bdCode, vo.getUCode(), 'Y');
                boardService.unRecoDownd(bdCode);
            } else {
                boardService.insertReco(bdCode, vo.getUCode(), 'Y');
            }
            boardService.recoUp(bdCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/unLike")
    public ResponseEntity<String> unLikeAdd(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        boolean reco = Boolean.parseBoolean(request.getParameter("check_reco"));
        boolean non_reco = Boolean.parseBoolean(request.getParameter("check_nonReco"));
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));

        if (non_reco) {
            boardService.deleteReco(bdCode, vo.getUCode());
            boardService.unRecoDownd(bdCode);
        } else {
            if (reco) {
                boardService.updateReco(bdCode, vo.getUCode(), 'N');
                boardService.recoDown(bdCode);
            } else {
                boardService.insertReco(bdCode, vo.getUCode(), 'N');
            }
            boardService.unRecoUp(bdCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/bookmark")
    public ResponseEntity<String> addBookmark(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String str = request.getParameter("bookmark");
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));

        if (str.equals("Y")) {
            boardService.insertBookmark(bdCode, vo.getUCode());
        } else {
            boardService.removeBookmark(bdCode, vo.getUCode());
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    @PostMapping("/delete")
    public ResponseEntity<String> deleteBoard(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));
        String uCode = vo.getUCode();
        log.info(bdCode);

        log.info(uCode.split("/")[1].equals("A"));
        if (uCode.split("/")[1].equals("A")) {
            boolean isUpdate = boardService.boardType(bdCode);
            try {
                if (isUpdate == true) {
                    log.info("수정 완료");
                }
            } catch (Exception e) {
                log.warn("에러 발생");
            }
        } else {
            try {
                boolean isDeleted = boardService.deleteBoard(bdCode, uCode);
                if (isDeleted == false) {
                    return new ResponseEntity<>("작성자 본인이 아닙니다!", HttpStatus.BAD_REQUEST);
                } else if (isDeleted == true) {
                    return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
                }
            } catch (DataAccessException e) {
                return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>("시스템 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("작성자 본인이 아닙니다!", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>("업데이트 완료", HttpStatus.OK);
    }

    @PostMapping("/delete/comment/{comCode}")
    public ResponseEntity<String> deleteComment(@PathVariable("comCode") String comCode, HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String comcode = HotpleAPI.strToCode(comCode);
        boolean isDeleted = commentService.commentDelete(comcode);

        if (vo.getUCode().split("/")[1].equals("A")) {
            boolean isUpdate = commentService.commentType(comCode);
            try {
                if (isUpdate == true) {
                    log.info("수정 완료");
                }
            } catch (Exception e) {
                log.warn("에러 발생");
            }
        } else {
            try {
                if (isDeleted == true) {
                    log.info("삭제 완료");
                }
            } catch (Exception e) {
                log.warn("에러 발생");
            }
            return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
        }
        return new ResponseEntity<>("업데이트 완료", HttpStatus.OK);
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportBoard(HttpServletRequest request, HttpSession session, ReportVO vo) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        vo.setUCode(vo1.getUCode());
        vo.setRepCont(request.getParameter("repCont"));
        vo.setRepKind(request.getParameter("repKind"));
        vo.setBdCode(request.getParameter("bdCode"));
        log.info(vo);
        boolean isInserted = report.reportBoard(vo);
        try {
            if (isInserted == true) {
                log.info("신고 완료");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("신고 완료", HttpStatus.OK);
    }

    @PostMapping("/comment/report")
    public ResponseEntity<String> reportComment(HttpServletRequest request, HttpSession session, ReportVO vo) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        vo.setUCode(vo1.getUCode());
        vo.setComCode(request.getParameter("comCode"));
        vo.setRepKind(request.getParameter("repKind"));
        vo.setRepCont(request.getParameter("repCont"));
        log.info(vo);
        boolean isInserted = report.reportComment(vo);
        try {
            if (isInserted == true) {
                log.info("신고 완료");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("신고 완료", HttpStatus.OK);
    }
}
