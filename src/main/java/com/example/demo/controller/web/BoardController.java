package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/write")
    public String openBoardWrite() {
        return "user/boardWrite";
    }

    @GetMapping("/update/{bdCode}")
    public String updateBoard(@PathVariable(value = "bdCode") String bdCode, Model model) {
        if (bdCode == null) {
            return "redirect:/board/list";
        } else{
            BoardVO vo = boardService.getBoardDetail(HotpleAPI.strToCode(bdCode));
            if (vo == null) {
                return "redirect:/board/list";
            }
            model.addAttribute("board", vo);
            return "user/boardWrite";
        }
    }
//    댓글 업뎃
//    @GetMapping("/update/{comCode}")
//    public String updateComment(@PathVariable(value = "comCode") String comCode, Model model) {
//        if (comCode == null) {
//            return "redirect:/board/view/{bdCode}";
//        } else{
//            CommentVO vo = commentService.commentUpdate(comCode);
//        }
//    }

    @GetMapping(value = "/list")
    public String boardList(@ModelAttribute("BoardVO") BoardVO boardVO, @RequestParam(value = "kind", defaultValue = "B") String kind, Model model) {
        List<BoardVO> boardList = boardService.getBoardList(boardVO);
        model.addAttribute("result", boardList);
        model.addAttribute("board", boardVO);

        return "user/board";
    }

//    @GetMapping("/boardDetail/{bdCode}")
//    public String boardDetail(@PathVariable("bdCode") String bdCode, CommentVO commentVO, Model model) {
//        log.debug("read request");
//        BoardVO vo = boardService.getBoardDetail(HotpleAPI.strToCode(bdCode));
//        model.addAttribute("board", vo);
//
//        return "user/boardView";
//    }

//    원용 게시글상세 컨트롤러
    @GetMapping("/view/{bdCode}")
    public String view(@PathVariable("bdCode") String bdCode, Model model) {
        log.info("read request");
        boardService.upView(HotpleAPI.strToCode(bdCode));
        BoardVO vo = boardService.getBoardDetail(HotpleAPI.strToCode(bdCode));

        model.addAttribute("board", vo);

        return "user/boardView2";
    }



    @GetMapping("/comment/{bdCode}")
    public String commentList(@PathVariable(value = "bdCode") String bdCode, @RequestParam(value = "sort", defaultValue = "reco") String sort, Model model) {
        log.info(bdCode);
        List<CommentVO> commentList = null;
        Map<CommentVO, List<CommentVO>> map = new LinkedHashMap<>();
        log.info(sort);
        if (sort.equals("reco")) {
            commentList = commentService.commOdByReco(HotpleAPI.strToCode(bdCode));
        } else if (sort.equals("recent")){
            commentList = commentService.commOdByRecent(HotpleAPI.strToCode(bdCode));
        } else if (sort.equals("writen")) {
            commentList = commentService.commOdByWritenReply(HotpleAPI.strToCode(bdCode));
        } else {
            return "redirect:/board/comment/" + bdCode;
        }

        List<CommentVO> temp = commentList;
        log.info(temp);
        commentList.forEach(i -> {
            if (i.getReplyCode() == null) {  // 댓글
                map.put(i, new ArrayList<>());
            } else {    // 답글
                for (CommentVO vo : temp) {
                    if (i.getReplyCode().equals(vo.getComCode())) {
                        map.get(vo).add(i);
                    }
                }
            }
        });
        log.info(map);

        model.addAttribute("commentList", map);
        return "user/comment";
    }


    @GetMapping(value = "/delete/{bdCode}")
    public String deleteBoard(@PathVariable(value = "bdCode") String bdCode) {
        if (bdCode == null) {
            return "redirec:/board/list";
        }
        try {
            boolean isDeleted = boardService.deleteBoard(HotpleAPI.strToCode(bdCode));
            if (isDeleted == false) {
                log.warn("게시글 삭제 실패");
            }
        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러 발생");
        }
        return "redirect:/board/list";
    }

    @GetMapping(value = "/delete/comment/{comCode}")
    public String deleteComment(@PathVariable(value = "comCode") String comCode) {
        log.info(comCode);
        if (comCode == null) {
            return "redirect:/board/list";
        }
        try {
            boolean isCommentDeleted = commentService.commentDelete(HotpleAPI.strToCode(comCode));
            if (isCommentDeleted == false) {
                log.warn("댓글 삭제 실패");
            }
        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러 발생");
        }
        return "redirect:/board/list";
    }
}