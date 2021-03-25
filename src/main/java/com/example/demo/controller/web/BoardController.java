package com.example.demo.controller.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.service.web.BoardService;
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

    @GetMapping("/write")
    public String openBoardWrite(@RequestParam(value = "bdCode", required = false) String bdCode, Model model) {
        model.addAttribute("board", new BoardVO());
        return "user/boardWrite";
    }

    @GetMapping(value = "/list")
    public String openBoardList(Model model) {
        List<BoardVO> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);

        return "user/board";
    }
}