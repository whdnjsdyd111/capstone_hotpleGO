package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.CourseVO;
import com.example.demo.domain.Criteria;
import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.PageVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.CourseService;
import com.example.demo.service.HotpleService;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final HotpleService hotpleService;
    private final CourseService courseService;

    @GetMapping("/write")
    public String openBoardWrite(String htId, String csCode) {
        return "user/boardWrite";
    }

    @GetMapping("/update/{bdCode}")
    public String updateBoard(@PathVariable(value = "bdCode") String bdCode, Model model, @AuthenticationPrincipal CustomUser user) {
        if (user == null) return "redirect:/board/list";
        String bd_code = HotpleAPI.strToCode(bdCode);
        if (bdCode == null || boardService.getCheck(bd_code, user.user.getUCode())) {
            return "redirect:/board/list";
        } else {
            BoardVO vo = boardService.getBoardDetail(bd_code);
            if (vo == null) {
                return "redirect:/board/list";
            }
            model.addAttribute("board", vo);
            return "user/boardWrite";
        }
    }

    @GetMapping("/list")
    public String boardList(Criteria cri, @RequestParam(value = "kind", defaultValue = "B") String kind, Model model) {
        String str = kind.equals("B") ? ("게시판") : (kind.equals("H") ? "핫플" : "코스");
        log.info(cri.toString());
        model.addAttribute("result", boardService.getBoardListN(cri));
        model.addAttribute("pageMaker", new PageVO(cri, boardService.getTotal(cri)));
        model.addAttribute("kind", str);
        return "user/board";
    }

    @GetMapping("/bookmark")
    public String bookmarkList(@ModelAttribute("BoardVO") BoardVO boardVO, Model model, @AuthenticationPrincipal CustomUser user, @RequestParam(value = "kind", defaultValue = "B") String kind) {
        String str = kind.equals("B") ? ("게시판") : (kind.equals("H") ? "핫플" : "코스");
        boardVO.setUCode(user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/");
        List<BoardVO> bookmarkList = boardService.getBookmarkList(boardVO);
        log.info(boardVO);
        model.addAttribute("result", bookmarkList);
        model.addAttribute("kind", str);
        return "user/bookmark";
    }


    @GetMapping("/view/{bdCode}")
    public String view(@PathVariable("bdCode") String bdCode, Model model, @AuthenticationPrincipal CustomUser user) {
        log.info("read request");
        boardService.upView(HotpleAPI.strToCode(bdCode));
        BoardVO vo = boardService.getBoardDetail(HotpleAPI.strToCode(bdCode));
        model.addAttribute("board", vo);


        if (vo.getHtId() != null) {
            HotpleVO hotpleVO = hotpleService.getId(String.valueOf(vo.getHtId()));
            model.addAttribute("hotple", hotpleVO);
        }

        if (vo.getCsCode() != null) {
            CourseVO courseVO = courseService.getCourseDetail(vo.getCsCode());
            model.addAttribute("course", courseVO);
            model.addAttribute("courseInfos", courseService.getCourseInfoDetail(vo.getCsCode()));
        }
        if (user != null) {
            String uCode = user.user.getUCode();
            model.addAttribute("reco", boardService.getReco(vo.getBdCode(), uCode));
            model.addAttribute("bookmark", boardService.getBookmark(vo.getBdCode(), uCode));
        }

        return "user/boardView2";
    }

    /*핫플공유 글쓰기*/
    @GetMapping("/shareHotple/{htId}")
    public String sharedHotpleWrite(@PathVariable("htId") String htId, Model model, @AuthenticationPrincipal CustomUser user) {
        HotpleVO hotple = this.hotpleService.getId(htId);
        hotple.setUCode(user.user.getUCode());
        model.addAttribute("hotple", hotple);
        return "user/shareWrite";
    }

    /*코스공유 글쓰기*/
    @GetMapping("/shareCourse/{csCode}")
    public String sharedCourseWrite(@PathVariable("csCode") String csCode, Model model, @AuthenticationPrincipal CustomUser user) {
        CourseVO course = this.courseService.getCourseDetail(HotpleAPI.strToCode(csCode));
        model.addAttribute("course", course);
        return "user/shareWrite";
    }

    @GetMapping("/comment/{bdCode}")
    public String commentList(@PathVariable(value = "bdCode") String bdCode, @RequestParam(value = "sort", defaultValue = "reco") String sort, Model model, @AuthenticationPrincipal CustomUser user) {
        log.info(bdCode);
        String bdcode = HotpleAPI.strToCode(bdCode);

        List<CommentVO> commentList = null;
        Map<CommentVO, List<CommentVO>> map = new LinkedHashMap<>();
        log.info(sort);
        if (sort.equals("reco")) {
            commentList = commentService.commOdByRecoN(bdcode);
        } else if (sort.equals("recent")) {
            commentList = commentService.commOdByRecent(bdcode);
        } else if (sort.equals("writen")) {
            commentList = commentService.commOdByWritenReply(bdcode);
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
        if (user != null) model.addAttribute("commReco", commentService.getComReco(bdcode, user.user.getUCode()));
        return "user/comment";
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