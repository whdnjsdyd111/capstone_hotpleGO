package com.example.demo.controller.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.service.web.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String openBoardWrite(@RequestParam(value = "bdCode", required = false) String bdCode, Model model) {
        if (bdCode == null) {
            model.addAttribute("board", new BoardVO());
        } else {
            BoardVO board = boardService.getBoardDetail(bdCode);
            if (board == null) {
                return "redirect:/board";
            }
            model.addAttribute("board", board);
        }

        return "/board";
    }

    @PostMapping(value = "/write")
    public String registerBoard(BoardVO params) {
        try {
            boolean isRegistered = boardService.registerBoard(params);
            if (isRegistered == false) {
                log.warn("게시글 등록 실패");
            }
        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러 발생");
        }
        return "/boardWrite";
    }
}