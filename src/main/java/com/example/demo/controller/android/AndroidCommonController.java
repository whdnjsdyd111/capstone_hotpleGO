package com.example.demo.controller.android;

import com.example.demo.domain.*;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.service.*;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/android/*")
@RequiredArgsConstructor
@Log4j2
public class AndroidCommonController {
    private final PasswordEncoder passwordEncoder;
    private final UserService users;
    private final TasteService taste;
    private final BoardService board;
    private final CommentService comment;
    private final ImageAttachService imageAttach;
    private final CourseService course;
    private final ReservationService reservation;
    private final ReviewService review;
    private final MenuService menu;
    private final HotpleService hotple;

    // 유저 정보

    @PostMapping("/login")
    public String login(HttpServletRequest request) throws JSONException {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        UserVO vo = users.getByEmail(id);
        if (vo == null) {
            return "{message: '아이디 또는 비밀번호가 틀렸습니다.'}";
        } else if (new BCryptPasswordEncoder().matches(pw, vo.getPw())) {
            JSONObject obj = new JSONObject();
            if (vo.getUCode().split("/")[1].equals("M")) obj.put("hotple", new JSONObject(hotple.getByUCode(vo.getUCode())));
            obj.put("user", new JSONObject(vo));
            obj.put("message", true);
            return obj.toString();
        } else {
            return "{message: '아이디 또는 비밀번호가 틀렸습니다.'}";
        }
    }

    @PostMapping("/check_email")
    public String checkEmail(HttpServletRequest request) {
        String email = request.getParameter("email");
        UserVO vo = users.getByEmail(email);

        if (vo == null) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/userJoin")
    public String userJoin(UserVO vo, HttpServletRequest request) {
        Date birth = null;
        try {
            birth = new SimpleDateFormat("yyMMdd").parse(request.getParameter("birth_str"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info(vo);

        vo.setUCode(vo.getUCode() + "/U/");
        vo.setPw(passwordEncoder.encode(vo.getPw()));
        vo.setBirth(birth);
        if (users.register(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/managerJoin")
    public String managerJoin(ManagerVO vo) {
        log.info(vo);

        vo.setUCode(vo.getUCode() + "/M/");
        vo.setPw(passwordEncoder.encode(vo.getPw()));
        if (users.registerManager(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    // 코스 정보

    @PostMapping("/myCourse")
    public String getUsingCourse(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String kind = request.getParameter("kind");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taste", taste.getTasteList(uCode).size() != 0);

        switch (kind) {
            case "usingCourse":
                jsonObject.put("courses", new JSONObject(course.getUsingCourse(uCode)));
                jsonObject.put("coursesInfos", course.getUsingCourseInfo(uCode));
                break;
            case "myCourse":
                jsonObject.put("courses", course.getMakingCourse(uCode));
                jsonObject.put("courseInfos", course.getMakingCourseInfo(uCode));
                break;
            case "usedCourse":
                jsonObject.put("courses", course.getHistoryCourse(uCode));
                jsonObject.put("courseInfos", course.getHistoryCourseInfo(uCode));
                break;
            case "dibs":
                jsonObject.put("courses", users.getPickCourseList(uCode));
                jsonObject.put("courseInfos", course.getDibsCourseInfo(uCode));
                break;
        }
        return jsonObject.toString();
    }

    // 핫플 공유

    @PostMapping("/getBoards")
    public String getBoards(HttpServletRequest request) {
        String keyword = request.getParameter("keyword");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boards", board.getBoardsByKeyword(keyword));
        return jsonObject.toString();
    }

    @PostMapping("/getBoard")
    public String getBoard(HttpServletRequest request) {
        String bdCode = request.getParameter("bdCode");
        String uCode = request.getParameter("uCode");
        board.upView(bdCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("board", new Gson().toJson(board.getBoardDetail(bdCode)));
        List<CommentVO> commentList = comment.commOdByRecoN(bdCode);
        Map<CommentVO, List<CommentVO>> map = new LinkedHashMap<>();
        List<CommentVO> temp = new ArrayList<>();
        commentList.forEach(i -> {
            if (i.getReplyCode() == null) {  // 댓글
                map.put(i, new ArrayList<>());
            } else {    // 답글
                for (CommentVO vo : commentList) {
                    if (i.getReplyCode().equals(vo.getComCode())) {
                        map.get(vo).add(i);
                    }
                }
            }
        });
        map.forEach((k ,v) -> {
            temp.add(k);
            v.forEach(vo -> temp.add(vo));
        });
        jsonObject.put("comment", new Gson().toJson(temp));
        if (uCode != null) {
            jsonObject.put("bookmark", board.getBookmark(bdCode, uCode));
            jsonObject.put("comReco", new Gson().toJson(comment.getComReco(bdCode, uCode)));
        }
        return jsonObject.toString();
    }

    @PostMapping("/insertBoard")
    public String insertBoard(HttpServletRequest request) {
        BoardVO vo = new Gson().fromJson(request.getParameter("board"), BoardVO.class);
        log.info(vo);
        if (board.insertBoard(vo)) return "{message: true}";
        else return "{message: false}";
    }

    @PostMapping("/bookmark")
    public String bookmark(HttpServletRequest request) {
        boolean bookmark = Boolean.parseBoolean(request.getParameter("bookmark"));
        String bdCode = request.getParameter("bdCode");
        String uCode = request.getParameter("uCode");

        if (bookmark) return "{message:" + board.removeBookmark(bdCode, uCode) + "}";
        else return "{message:" + board.insertBookmark(bdCode, uCode) + "}";
    }

    @PostMapping("/deleteBoard")
    public String deleteBoard(HttpServletRequest request) {
        String bdCode = request.getParameter("bdCode");
        String uCode = request.getParameter("uCode");

        return "{message:" + board.deleteBoard(bdCode, uCode) + "}";
    }

    @PostMapping("/updateBoard")
    public String updateBoard(HttpServletRequest request) {
        BoardVO vo = new Gson().fromJson(request.getParameter("board"), BoardVO.class);
        log.info(vo);
        String uCode = request.getParameter("uCode");
        if (board.updateBoard(vo, uCode)) return "{message: true}";
        else return "{message: false}";
    }

    @PostMapping("/com_reco")
    public String comReco(HttpServletRequest request) {
        String comCode = request.getParameter("comCode");
        String uCode = request.getParameter("uCode");
        boolean reco = Boolean.parseBoolean(request.getParameter("reco"));
        boolean nonReco = Boolean.parseBoolean(request.getParameter("nonReco"));

        if (reco) {
            comment.deleteComReco(comCode, uCode);
            comment.comRecoDown(comCode);
            return "{message: ''}";
        } else {
            if (nonReco) {
                comment.updateComReco(comCode, uCode, 'Y');
                comment.unComRecoDown(comCode);
            } else {
                comment.insertComReco(comCode, uCode, 'Y');
            }
            comment.comRecoUp(comCode);
            return "{message: 'Y'}";
        }
    }

    @PostMapping("/com_nonReco")
    public String comNonReco(HttpServletRequest request) {
        String comCode = request.getParameter("comCode");
        String uCode = request.getParameter("uCode");
        boolean reco = Boolean.parseBoolean(request.getParameter("reco"));
        boolean nonReco = Boolean.parseBoolean(request.getParameter("nonReco"));

        if (nonReco) {
            comment.deleteComReco(comCode, uCode);
            comment.unComRecoDown(comCode);
            return "{message: ''}";
        } else {
            if (Boolean.parseBoolean(request.getParameter("reco"))) {
                comment.updateComReco(comCode, uCode, 'N');
                comment.comRecoDown(comCode);
            } else {
                comment.insertComReco(comCode, uCode, 'N');
            }
            comment.unComRecoUp(comCode);
            return "{message: 'N'}";
        }
    }

    @PostMapping("/com_submit")
    public String comSubmit(HttpServletRequest request) {
        CommentVO vo = new CommentVO();
        vo.setUCode(request.getParameter("uCode"));
        vo.setBdCode(request.getParameter("bdCode"));
        vo.setComCont(request.getParameter("comCont"));
        vo.setReplyCode(request.getParameter("replyCode"));
        if (comment.commentInsert(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    // 예약

    @PostMapping("/reservation")
    public String reservation(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String uCode = request.getParameter("uCode");
        jsonObject.put("reservation",
                new Gson().toJson(reservation.getHotples(uCode)));
        jsonObject.put("reservations",
                new Gson().toJson(reservation.getList(uCode)));
        return jsonObject.toString();
    }

    @PostMapping("/get_review")
    public String getReview(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        ReviewVO vo = review.getReview(request.getParameter("riCode"));
        if (vo == null)
            jsonObject.put("message", false);
        else {
            jsonObject.put("message", true);
            jsonObject.put("review", new Gson().toJson(vo));
        }
        return jsonObject.toString();
    }

    @PostMapping("/submit_review")
    public String submitReview(HttpServletRequest request) {
        ReviewVO vo = new ReviewVO();
        vo.setRiCode(request.getParameter("riCode"));
        vo.setRvRating(Integer.parseInt(request.getParameter("rvRating")));
        vo.setRvCont(request.getParameter("rvCont"));
        vo.setUCode(request.getParameter("uCode"));
        if (review.registerReview(vo)) return "{message: true}";
        else return "{message: false}";
    }

    @PostMapping("/update_review")
    public String updateReview(HttpServletRequest request) {
        ReviewVO vo = new ReviewVO();
        vo.setRiCode(request.getParameter("riCode"));
        vo.setRvRating(Integer.parseInt(request.getParameter("rvRating")));
        vo.setRvCont(request.getParameter("rvCont"));
        if (review.updateReview(vo)) return "{message: true}";
        else return "{message: false}";
    }

    // 사용자 정보 관련 메소드

        // 사용자 취향 관련 메소드
    @PostMapping("/getTaste")
    public String getTaste(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tastes", taste.getTasteList(uCode));
        return jsonObject.toString();
    }

    @PostMapping("/saveTaste")
    public String saveTaste(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String tastes = request.getParameter("tastes");
        tastes = tastes.replaceAll("]\"", "]").replaceAll("\"\\[", "[");
        JSONObject jsonObject = new JSONObject(tastes);
        JSONArray jsonArray = jsonObject.getJSONArray("tastes");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getInt(i));
        }
        taste.reset(uCode);
        if (taste.registerAll(uCode, list)) {
            return "{message: '취향을 저장하였습니다.'}";
        } else {
            return "{message: '실패하였습니다. 다시 시도해주십시오.'}";
        }
    }

        // 사용자 MBTI 선택
    @PostMapping("/getMbti")
    public String getMbti(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String mbti = users.getMbti(uCode);
        if (mbti == null)
            return "{mbti: ''}";
        else
            return "{mbti:'" + mbti + "'}";
    }

    @PostMapping("/saveMbti")
    public String saveMbti(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String mbti = request.getParameter("mbti");
        if (users.updateMbti(mbti, uCode)) {
            return "{message:'" + mbti + "'}";
        } else {
            return "{message:'다시 시도해주십시오.'}";
        }
    }

    @PostMapping("/changeNick")
    public String changeNick(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String nick = request.getParameter("nick");
        if (users.updateNick(nick, uCode)) {
            return "{message:'수정 완료하였습니다.', status:200}";
        } else {
            return "{message:'다시 시도해주십시오.', status:500}";
        }
    }

    // 북마크
    @PostMapping("/getBookmark")
    public String getBookmark(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        BoardVO vo = new BoardVO();
        vo.setUCode(uCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boards", board.getBookmarkList(vo));
        return jsonObject.toString();
    }

    // 이미지 업로드
    @PostMapping("/upload")
    public String upload(MultipartHttpServletRequest request) {
        MultipartFile image = request.getFile("upload");
        ImageAttachVO vo = new ImageAttachVO();
        String url = vo.upload(image);
        log.info(url);
        imageAttach.upload(vo);
        return new JSONObject().put("file", url).toString();
    }

        // 업체 운영자
    // 메뉴
    @PostMapping("/get_menus")
    public String getMenus(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menus",
                new Gson().toJson(menu.getListByUser(request.getParameter("uCode"))));
        return jsonObject.toString();
    }

    @PostMapping("/add_menu")
    public String addMenu(MultipartHttpServletRequest request) {
        MultipartFile upload = request.getFile("upload");
        MenuVO vo = new Gson().fromJson(request.getParameter("menu"), MenuVO.class);

        ImageAttachVO imageAttachVO = new ImageAttachVO();
        if (upload != null) {
            imageAttachVO.upload(upload);
            if (imageAttach.upload(imageAttachVO)) {
                vo.setUuid(imageAttachVO.getUuid());
                vo.setUploadPath(imageAttachVO.getUploadPath());
                vo.setFileName(imageAttachVO.getFileName());
            }
        }
        if (menu.register(vo)) {
            log.info(vo);
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/update_menu")
    public String updateMenu(MultipartHttpServletRequest request) {
        MultipartFile upload = request.getFile("upload");
        MenuVO vo = new Gson().fromJson(request.getParameter("menu"), MenuVO.class);

        if (upload == null) {
            if (menu.update(vo)) {
                return "{message: true}";
            } else {
                return "{message: false}";
            }
        } else {
            ImageAttachVO imageAttachVO = new ImageAttachVO();
            imageAttachVO.upload(upload);

            if (vo.getUuid().isEmpty()) {
                if (imageAttach.upload(imageAttachVO)) {
                    vo.setUuid(imageAttachVO.getUuid());
                    vo.setUploadPath(imageAttachVO.getUploadPath());
                    vo.setFileName(imageAttachVO.getFileName());
                    if (!menu.updateOnlyImage(vo)) {
                        return "{message: true}";
                    }
                }
            } else {
                menu.updateWithImage(vo, imageAttachVO);
                imageAttachVO.deleteFiles(vo.getUuid(), vo.getUploadPath(), vo.getFileName());
                vo.setUuid(imageAttachVO.getUuid());
                vo.setUploadPath(imageAttachVO.getUploadPath());
                vo.setFileName(imageAttachVO.getFileName());
            }
            return "{message: true}";
        }
    }

    // 오더
    @PostMapping("/orders")
    public String orders(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        Map<String, List<ReservationAllVO>> map = reservation.getListByManager(uCode);
        List<ReviewVO> list = review.getListByManager(uCode);
        Map<String, ReviewVO> reviewMap = new HashMap<>();
        list.forEach(l -> reviewMap.computeIfAbsent(l.getRiCode(), k -> l));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orders", new JSONObject(map));
        jsonObject.put("reviews", new JSONObject(reviewMap));
        return jsonObject.toString();
    }

    @PostMapping("/manager_review")
    public String managerReview(HttpServletRequest request) {
        String riCode = request.getParameter("riCode");
        String rvOwnCont = request.getParameter("rvOwnCont");
        ReviewVO vo = new ReviewVO();
        vo.setRiCode(riCode);
        vo.setRvOwnCont(rvOwnCont);
        if (review.changeRvOwnCont(vo)) return "{message: true}";
        else return "{message: false}";
    }

    @PostMapping("/manager_hotple")
    public String managerHotple(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        JSONObject jsonObject = new JSONObject();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss.SSS").create();
        jsonObject.put("hotple", gson.toJson(hotple.getByUCode(uCode)));
        jsonObject.put("manager", gson.toJson(users.getManager(uCode)));
        return jsonObject.toString();
    }

    @PostMapping("/bank_update")
    public String bankUpdate(HttpServletRequest request) {
        ManagerVO vo = new ManagerVO();
        vo.setUCode(request.getParameter("uCode"));
        vo.setMAccount(request.getParameter("account"));
        vo.setMBank(request.getParameter("bank"));
        if (users.updateAccount(vo)) return "{message: true}";
        else return "{message: false}";
    }

    @PostMapping("/nick_update")
    public String nickUpdate(HttpServletRequest request) {
        if (users.updateNick(request.getParameter("nick"),
                request.getParameter("uCode"))) return "{message: true}";
        else return "{message: false}";
    }

    @PostMapping("/comp_update")
    public String compUpdate(MultipartHttpServletRequest request) {
        MultipartFile upload = request.getFile("upload");
        HotpleVO vo = new Gson().fromJson(request.getParameter("hotple"), HotpleVO.class);
        ImageAttachVO imageAttachVO = new ImageAttachVO();
        if (upload != null) {
            imageAttachVO.upload(upload);
            hotple.updateWithImage(vo, imageAttachVO);
            imageAttachVO.deleteFiles(vo.getHtImg(), vo.getUploadPath(), vo.getFileName());
        } else {
            if (!hotple.update(vo))
                return "{message: false}";
        }
        return "{message: true}";
    }
}
