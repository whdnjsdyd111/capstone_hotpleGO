package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.service.ImageAttachService;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
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
    public ResponseEntity<String> insertBoard(@RequestBody BoardVO boardVO) {
        boardVO.setUCode("tmdghks111@naver.com/U/");
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

    @PostMapping("/view")
    public ResponseEntity<String> insertComment(@RequestBody CommentVO commentVO) {
        commentVO.setUCode("tmdghks111@naver.com/U/");
        commentVO.setBdCode("210404104338/04/N");
        commentVO.setReplyCode(commentVO.getReplyCode());
        log.info(commentVO);
        boolean isCommInserted = commentService.commentInsert(commentVO);
        try {
            if (isCommInserted == true) {
                log.info("댓글 등록 성공");
            }
        } catch (DataAccessException e){
            log.warn("데이터 처리 실패");
        } catch (Exception e){
            log.warn("시스템 에러");
        }
        return new ResponseEntity<>("댓글 등록이 완료되었습니다.", HttpStatus.OK);
    }


    @PostMapping(value = "/update")
    public ResponseEntity<String> updateBoard(@RequestBody BoardVO boardVO, Model model) {
        log.info(boardVO);
        boolean data = boardService.updateBoard(boardVO);
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

//    @PostMapping(value = "/comment")
//    public ResponseEntity<String> updateComment(@RequestBody CommentVO commentVO, Model model) {
//        boolean comData = commentService.commentUpdate(commentVO);
//        model.addAttribute("data", comData);
//        return new ResponseEntity<>("댓글 수정이 완료되었습니다.", HttpStatus.OK);
//    }
}
