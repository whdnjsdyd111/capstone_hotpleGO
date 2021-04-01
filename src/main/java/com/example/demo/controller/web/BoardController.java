package com.example.demo.controller.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public String openBoardWrite(@RequestParam(value = "bdCode", required = false)String bdCode, Model model) {
        if (bdCode == null) {
            model.addAttribute("board", new BoardVO());
        } else{
            BoardVO vo = boardService.getBoardDetail(bdCode);
            if (vo == null) {
                return "redirect:/board/list";
            }
            model.addAttribute("board", vo);
        }
        return "user/boardWrite";
    }

    @GetMapping(value = {"/","/list"})
    public String boardList(@ModelAttribute("BoardVO") BoardVO boardVO, Model model) {
        List<BoardVO> boardList = boardService.getBoardList(boardVO);
        model.addAttribute("result", boardList);
        model.addAttribute("board", boardVO);
//        List<CommentVO> commentList = commentService.commentList(commentVO);
//        model.addAttribute("commentList", commentList);

        return "user/board";
    }

    @GetMapping({"/boardDetail", "update"})
    public String boardDetail(@ModelAttribute("bdCode") String bdCode, Model model) {
        log.debug("read request");
        BoardVO vo = boardService.getBoardDetail(bdCode);
        model.addAttribute("board", vo);

        return "/user/boardView";
    }
}