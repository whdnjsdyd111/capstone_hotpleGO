package com.example.demo.controller.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.service.web.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/rest/*")
@RequiredArgsConstructor
@Log4j2
public class BoardRestController {
    private final BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<String> insertBoard(@RequestBody BoardVO boardVO) {
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

    @PostMapping(value = "/board/update/{code}")
    public ResponseEntity<String> updateBoard(@PathVariable(value = "code") BoardVO boardVO, Model model) {
        boolean data = boardService.updateBoard(boardVO);
        model.addAttribute("data", data);
        return new ResponseEntity<>("글 수정이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping(value = "/board/delete/{code}")
    public String deleteBoard(@PathVariable(value = "code") String bdCode) {
        if (bdCode == null) {
            return "redirect:/board/list";
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
