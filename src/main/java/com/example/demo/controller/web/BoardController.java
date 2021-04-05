package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    private final BoardService boardService;
//    private final CommentService commentService;

    @GetMapping("/write")
    public String openBoardWrite() {
        return "user/boardWrite";
    }

    @GetMapping("/update/{bdCode}")
    public String updateBoard(@PathVariable(value = "bdCode") String bdCode, Model model) {
        if (bdCode == null) {
            return "redirect:/board/";
        } else{
            BoardVO vo = boardService.getBoardDetail(HotpleAPI.strToCode(bdCode));
            if (vo == null) {
                return "redirect:/board/list";
            }
            model.addAttribute("board", vo);
            return "user/boardWrite";
        }
    }

    @GetMapping(value = "/list")
    public String boardList(@ModelAttribute("BoardVO") BoardVO boardVO, @RequestParam(value = "kind", defaultValue = "B") String kind, Model model) {
        List<BoardVO> boardList = boardService.getBoardList(boardVO);
        model.addAttribute("result", boardList);
        model.addAttribute("board", boardVO);
//        List<CommentVO> commentList = commentService.commentList(commentVO);
//        model.addAttribute("commentList", commentList);

        return "user/board";
    }

    @GetMapping("/boardDetail/{bdCode}")
    public String boardDetail(@PathVariable("bdCode") String bdCode, Model model) {
        log.debug("read request");
        BoardVO vo = boardService.getBoardDetail(HotpleAPI.strToCode(bdCode));
        model.addAttribute("board", vo);

        return "/user/boardView";
    }

    @GetMapping(value = "/delete/{bdCode}")
    public String deleteBoard(@PathVariable(value = "bdCode") String bdCode) {
        if (bdCode == null) {
            return "redirect:/board/list";
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

        return "redirect:/board/";
    }
}