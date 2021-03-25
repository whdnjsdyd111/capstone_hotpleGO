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

    @PostMapping(value = "/write")
    public String writeBoard(BoardVO boardVO) {
        try {
            boolean isRegistered = boardService.writeBoard(boardVO);
            if (isRegistered == false) {
                log.warn("게시글 등록 실패");
            }
        } catch (DataAccessException e) {
            log.warn("데이터 처리 실패");
        } catch (Exception e) {
            log.warn("시스템 에러 발생");
        }
        return "/user/boardWrite";
    }

    @GetMapping(value = "/board")
    public String openBoardList(Model model) {
        List<BoardVO> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);

        return "user/board";
    }

    @PostMapping(value = "/board/delete.do")
    public String deleteBoard(@RequestParam(value = "bdCode", required = false) String bdCode) {
        if (bdCode == null) {
            return "redirect:/board/list.do";
        }

        try {
            boolean isDeleted = boardService.deleteBoard(bdCode);
            if (isDeleted == false) {
                log.warn("게시글 삭제 실패");
            }
        } catch (DataAccessException e) {
                log.warn("데이터 처리 실패");

        } catch (Exception e) {
                log.warn("시스템 에러 발생");
        }

        return "redirect:/user/board";
    }
}