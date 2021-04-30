package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.GuideService;
import com.example.demo.service.ImageAttachService;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oracle.ucp.proxy.annotation.Post;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/board/rest/*")
@RequiredArgsConstructor
@Log4j2
public class BoardRestController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final ImageAttachService imageAttach;

    @PostMapping("/write")
    public ResponseEntity<String> insertBoard(@RequestBody BoardVO boardVO, @AuthenticationPrincipal CustomUser user) {
        boardVO.setUCode(user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/");
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

    @PostMapping("/view/{bdCode}")
    public ResponseEntity<String> insertComment(@RequestBody CommentVO commentVO, @PathVariable(value = "bdCode") String bdCode, @AuthenticationPrincipal CustomUser user) {
        commentVO.setUCode(user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/");
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

    @PostMapping(value = "/update")
    public ResponseEntity<String> updateBoard(@RequestBody BoardVO boardVO, Model model, @AuthenticationPrincipal CustomUser user) {
        log.info(boardVO);
        boolean data = boardService.updateBoard(boardVO, user.user.getUCode());
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
    public ResponseEntity<String> comLikeAdd(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        boolean com_reco = Boolean.parseBoolean(request.getParameter("check_com_reco"));
        boolean com_non_reco = Boolean.parseBoolean(request.getParameter("check_com_nonReco"));
        String comCode = HotpleAPI.strToCode(request.getParameter("comCode"));

        if (com_reco) {
            commentService.deleteComReco(comCode, user.user.getUCode());
            commentService.comRecoDown(comCode);
        } else {
            if (com_non_reco) {
                commentService.updateComReco(comCode, user.user.getUCode(), 'Y');
                commentService.unComRecoDown(comCode);
            } else {
                commentService.insertComReco(comCode, user.user.getUCode(), 'Y');
            }
            commentService.comRecoUp(comCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/unComLike")
    public ResponseEntity<String> unComLikeAdd(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        boolean com_reco = Boolean.parseBoolean(request.getParameter("check_com_reco"));
        boolean com_non_reco = Boolean.parseBoolean(request.getParameter("check_com_nonReco"));
        String comCode = HotpleAPI.strToCode(request.getParameter("comCode"));

        if (com_non_reco) {
            commentService.deleteComReco(comCode, user.user.getUCode());
            commentService.unComRecoDown(comCode);
        }else {
            if (com_reco) {
                commentService.updateComReco(comCode, user.user.getUCode(), 'N');
                commentService.comRecoDown(comCode);
            } else {
                commentService.insertComReco(comCode, user.user.getUCode(), 'N');
            }
            commentService.unComRecoUp(comCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeAdd(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        boolean reco = Boolean.parseBoolean(request.getParameter("check_reco"));
        boolean non_reco = Boolean.parseBoolean(request.getParameter("check_nonReco"));
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));

        if (reco) {
            boardService.deleteReco(bdCode, user.user.getUCode());
            boardService.recoDown(bdCode);
        } else {
            if (non_reco) {
                boardService.updateReco(bdCode, user.user.getUCode(), 'Y');
                boardService.unRecoDownd(bdCode);
            } else {
                boardService.insertReco(bdCode, user.user.getUCode(), 'Y');
            }
            boardService.recoUp(bdCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/unLike")
    public ResponseEntity<String> unLikeAdd(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        boolean reco = Boolean.parseBoolean(request.getParameter("check_reco"));
        boolean non_reco = Boolean.parseBoolean(request.getParameter("check_nonReco"));
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));

        if (non_reco) {
            boardService.deleteReco(bdCode, user.user.getUCode());
            boardService.unRecoDownd(bdCode);
        } else {
            if (reco) {
                boardService.updateReco(bdCode, user.user.getUCode(), 'N');
                boardService.recoDown(bdCode);
            } else {
                boardService.insertReco(bdCode, user.user.getUCode(), 'N');
            }
            boardService.unRecoUp(bdCode);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/bookmark")
    public ResponseEntity<String> addBookmark(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        String str = request.getParameter("bookmark");
        String bdCode = HotpleAPI.strToCode(request.getParameter("bdCode"));

        if (str.equals("Y")) {
            boardService.insertBookmark(bdCode, user.user.getUCode());
        } else {
            boardService.removeBookmark(bdCode, user.user.getUCode());
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteBoard(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        String bdCode = request.getParameter("bdCode");
        String uCode = user.user.getUCode();

        try {
            boolean isDeleted = boardService.deleteBoard(HotpleAPI.strToCode(bdCode), uCode);
            if (isDeleted == false) {
                return new ResponseEntity<>("작성자 본인이 아닙니다!", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("시스템 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("작성자 본인이 아닙니다!", HttpStatus.BAD_REQUEST);
    }
}
