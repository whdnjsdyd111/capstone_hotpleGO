package com.example.demo.controller.android;

import com.example.demo.domain.*;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.service.*;
import com.example.demo.service.web.BoardService;
import com.example.demo.service.web.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
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
    private final OpenInfoService openInfo;

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
            if (vo.getUCode().split("/")[1].equals("M"))
                obj.put("hotple", new JSONObject(hotple.getByUCode(vo.getUCode())));
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
        vo.setBirth(birth);
        if (users.register(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/socialRegister")
    public String socialJoin(UserVO vo, HttpServletRequest request) {
        log.info(vo);
        Date birth = null;
        try {
            birth = new SimpleDateFormat("yyMMdd").parse(request.getParameter("birth_str"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info(vo);

        vo.setUCode(request.getParameter("email") + "/U/" + request.getParameter("socialType"));
        vo.setBirth(birth);
        if (users.register(vo)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/socialLogin")
    public String socialEmailCheck(HttpServletRequest request) {
        String uCode = request.getParameter("email") + "/U/" + request.getParameter("socialType");
        UserVO vo = users.get(uCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", new JSONObject(vo));
        jsonObject.put("message", vo == null ? "false" : "true");
        return jsonObject.toString();
    }

    @PostMapping("/managerJoin")
    public String managerJoin(ManagerVO vo) {
        log.info(vo);

        vo.setUCode(vo.getUCode() + "/M/");
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
                CourseVO vo = course.getUsingCourse(uCode);
                jsonObject.put("courses", vo == null ? null : new JSONObject(vo));
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
        map.forEach((k, v) -> {
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
        if (users.aleadyNick(nick) == null) {
            if (users.updateNick(nick, uCode)) {
                return "{message:'수정 완료하였습니다.', status:200}";
            } else {
                return "{message:'다시 시도해주십시오.', status:500}";
            }
        } else {
            return "{message: '이미 존재하는 닉네임입니다.', status:500}";
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

            if (vo.getUuid() == null) {
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
        String nick = request.getParameter("nick");
        if (users.aleadyNick(nick) == null) {
            if (users.updateNick(nick,
                    request.getParameter("uCode"))) return "{message: true}";
            else return "{message: false}";
        } else {
            return "{message: false}";
        }
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

    @PostMapping("/sales")
    public String sales(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        Map<String, List<ReservationAllVO>> map =
                reservation.getSales(uCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sales", new JSONObject(map));
        return jsonObject.toString();
    }

    @PostMapping("/manager_management")
    public String managerManagement(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        Map<String, String[]> map = openInfo.getListByManager(uCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openInfo", new org.json.simple.JSONObject(map));
        return jsonObject.toString();
    }

    @PostMapping("/manger_management_update")
    public String managerManagementUpdate(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String wo = request.getParameter("wost").replace(":", "") + "/" + request.getParameter("woet").replace(":", "");
        String wb = request.getParameter("wbst").replace(":", "") + "/" + request.getParameter("wbet").replace(":", "");
        int kind = 0;
        switch (request.getParameter("kind")) {
            case "평일":
                kind = OpenInfoService.WEEKDAY;
                break;
            case "토요일":
                kind = OpenInfoService.SATURDAY;
                break;
            case "일요일":
                kind = OpenInfoService.SUNDAY;
                break;
        }

        if (openInfo.mergeOpen(wo, uCode, kind) && openInfo.mergeBreak(wb, uCode, kind)) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/search_hotple")
    public String searchHotple(HttpServletRequest request) {
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hotples", hotple.getByKeywordGeo(request.getParameter("keyword"),
                lat, lng));
        return jsonObject.toString();
    }

    @PostMapping("/get_open")
    public String getOpenInfo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openInfos", openInfo.getList(Long.parseLong(request.getParameter("htId"))));
        return jsonObject.toString();
    }

    @PostMapping("/hotpleReviews")
    public String hotpleReviews(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String htId = request.getParameter("htId");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reviews", review.getList(Long.parseLong(htId)));
        if (uCode != null) jsonObject.put("pick", users.getPickHotple(uCode, htId) != null);
        return jsonObject.toString();
    }


    @PostMapping("/hotpleMenus")
    public String hotpleMenu(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menus", menu.getList(request.getParameter("htId")));
        return jsonObject.toString();
    }

    @PostMapping("/hotpleInfos")
    public String hotpleInfos(HttpServletRequest request) {
        long htId = Long.parseLong(request.getParameter("htId"));
        List<MenuVO> list = menu.getList(String.valueOf(htId));
        Map<String, List<MenuVO>> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getMeCate(), k -> new ArrayList<>()).add(l));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("max", list.stream().mapToLong(MenuVO::getMePrice).max().orElse(0));
        jsonObject.put("min", list.stream().mapToLong(MenuVO::getMePrice).min().orElse(0));
        jsonObject.put("avg", list.stream().mapToLong(MenuVO::getMePrice).average().orElse(0));
        jsonObject.put("hotple", openInfo.getList(htId));
        return jsonObject.toString();
    }

    @Transactional
    @PostMapping("/res_order")
    public String resOrder(HttpServletRequest request) {
        List<MenuOrder> orders = new Gson().fromJson(request.getParameter("menuOrders"),
                new TypeToken<List<MenuOrder>>() {}.getType());
        String riTime = request.getParameter("riTime");
        String riPerson = request.getParameter("riPerson");
        String riOdNum = request.getParameter("riOdNum");
        String riCont = request.getParameter("riCont");
        String uCode = request.getParameter("uCode");
        String rName = request.getParameter("rName");

        ReservationInfoVO info = ReservationInfoVO.builder().riTime(Timestamp.valueOf(riTime))
                .riPerson(Short.valueOf(riPerson)).riOdNum(riOdNum).riCont(riCont).uCode(uCode).rName(rName)
                .htId(Long.parseLong(orders.get(0).getMenu().getMeCode().split("/")[0])).build();
        if (reservation.registerRes(info)) {
            List<ReservationStatusVO> list = new ArrayList<>();
            for (int i = 0; i < orders.size(); i++) {
                list.add(ReservationStatusVO.builder().riCode(info.getRiCode())
                        .meCode(orders.get(i).getMenu().getMeCode())
                        .rsMeNum(orders.get(i).getNum())
                        .uCode(uCode).build());
            }
            if (reservation.registerResStatus(list)) {
                return "{message: true}";
            }
        }
        return "{message: false}";
    }

    @PostMapping("/pick-delete")
    public String deletePickHotple(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String htId = request.getParameter("htId");

        boolean isDeleted = users.deletePickHotple(htId, uCode);
        try {
            if (isDeleted == true) {
                log.info("삭제 완료");
            }
        } catch (Exception e) {
            log.info("에러 발생");
        }
        return "{message: true}";
    }

    @PostMapping("/pickCourse-delete")
    public String deletePickCourse(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String csCode = request.getParameter("csCode");

        boolean isDeleted = users.deletePickCourse(csCode, uCode);
        try {
            if (isDeleted == true) {
                log.info("삭제 완료");
            }
        } catch (Exception e) {
            log.info("에러 발생");
        }
        return "{message: true}";
    }

    @PostMapping("/pick-hotple")
    public String pickHotple(HttpServletRequest request) {
        PickListVO vo = new PickListVO();
        vo.setUCode(request.getParameter("uCode"));
        vo.setHtId(request.getParameter("htId"));

        boolean isInserted = users.pickHotple(vo);
        try {
            if (isInserted == true) {
                log.info("성공");
            }
        } catch (Exception e) {
            return "{message: false}";
        }
        return "{message: true}";
    }

    @PostMapping("/pick-list")
    public String pickHotpleList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("picks", users.getPickHotpleList(request.getParameter("uCode")));
        return jsonObject.toString();
    }

    @PostMapping("/pick-course")
    public String pickCourse(HttpServletRequest request) {
        PickListVO vo = new PickListVO();
        vo.setUCode(request.getParameter("uCode"));
        vo.setCsCode(request.getParameter("csCode"));

        boolean isInserted = users.pickCourse(vo);

        try {
            if (isInserted == true) {
                log.info("성공");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return "{message: true}";
    }

    @Transactional
    @PostMapping("/course-copy")
    public String copyCourse(HttpServletRequest request) {
        String csCode = request.getParameter("csCode");
        String csWith = request.getParameter("csWith");
        String csNum = request.getParameter("csNum");
        String csTitle = request.getParameter("csTitle");

        CourseVO vo = new CourseVO();
        vo.setCsWith(csWith);
        vo.setCsNum(Byte.valueOf(csNum));
        vo.setCsTitle(csTitle);
        vo.setUCode(request.getParameter("uCode"));

        if (course.register(vo)) {
            if (course.copyCourse(vo.getCsCode(), csCode) > 0) {
                return "{message: true}";
            }
        }
        return "{message: false}";
    }

    @PostMapping("/use-course")
    public String useCourse(HttpServletRequest request) {
        String csCode = request.getParameter("csCode");
        String uCode = request.getParameter("uCode");
        CourseVO vo = course.getUsingCourse(uCode);
        if (vo == null) {
            course.changeUseCourse(csCode);
        } else {
            course.changeCourse(uCode, csCode);
        }
        return "{message: true}";
    }

    @PostMapping("/return-course")
    public String returnCourse(HttpServletRequest request) {
        course.returnCourse(request.getParameter("csCode"));
        return "{message: true}";
    }

    @PostMapping("/complete-course")
    public String completeCourse(HttpServletRequest request) {
        course.completeCourse(request.getParameter("csCode"));
        return "{message: true}";
    }

    @PostMapping("/delete-course")
    public String deleteCourse(HttpServletRequest request) {
        if (course.removeCourse(request.getParameter("csCode"))) {
            return "{message: true}";
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/delete-hotple-in-course")
    public String deleteHotpleInCourse(HttpServletRequest request) {
        course.removeHtInCs(request.getParameter("csCode"), request.getParameter("htId"));
        return "{message: true}";
    }

    @PostMapping("/add-in-course")
    public String addInCourse(HttpServletRequest request) {
        String csCode = request.getParameter("csCode");
        String htId = request.getParameter("htId");
        if (!course.alreadyAdded(csCode, htId)) {
            if (course.addCourse(csCode, htId)) {
                return "{message: true}";
            } else {
                return "{message: false}";
            }
        } else {
            return "{message: false}";
        }
    }

    @PostMapping("/around")
    public String around(HttpServletRequest request) {
        String uCode = request.getParameter("uCode");
        String urlOnOff = uCode == null ? "off" : "on";
        List<HotpleVO> filteredHotple = new ArrayList<>();
        List<HotpleVO> filteredHotpleM = new ArrayList<>();
        Map<String, List<HotpleVO>> filteredCourse = new HashMap<>();
        Map<String, List<HotpleVO>> filteredCourseM = new HashMap<>();
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));

        try {
            HttpURLConnection conn = null;
            URL url = new URL("http://127.0.0.1:5000/" + urlOnOff); // 액세스 토큰 받을 주소 입력

            String mbti = "[{mbti: ";
            if (uCode != null) {
                mbti = request.getParameter("mbti");
                if (mbti == null) return "{message: 'mbti 를 설정해주십시오.'}";
            }
            mbti += "}]";

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");  // post 방식으로 요청

            // 헤더 설정
            conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

            List<HotpleVO> hotples = hotple.getByGeoAndArea(lat, lng, 3);
            List<CourseVO> courses = course.getCourses();
            List<CourseInfoVO> courseInfos = course.getCourseInfos();

            // 키 설정
            String hotples_json = new Gson().toJson(hotples);
            String courses_json = new Gson().toJson(courses);
            String courseInfos_json = new Gson().toJson(courseInfos);

            // JSON 화한 키 값들 전달하기
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write("{" + hotples_json + "," + courses_json + "pp," + courseInfos_json + ",oo" + mbti + "}");   //json 객체로 1차적으로 다듬어서 보냄.
            bw.flush();
            bw.close();

            int responseCode = conn.getResponseCode();
            log.info("응답 코드 : " + responseCode);

            if (responseCode == 200) {  // 성공
                log.info("메인 받기 성공");
                // 버퍼 리더로 반환 값 얻기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                // 차례로 읽기
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
//                log.info("" + sb.toString());

                String str = sb.toString();
//                log.info(str);

                JSONArray array = new JSONArray(str);

                JSONArray hotpleArr = array.getJSONArray(0);
                int arr[] = new int[hotpleArr.length()];
                for (int i = 0; i < hotpleArr.length(); i++) {
                    arr[i] = hotpleArr.getInt(i);
                }
                hotples.stream().filter(n -> isMatch(n, arr)).forEach(filteredHotple::add);
//                log.info(filteredHotple);

                org.json.JSONObject courseObj = array.getJSONObject(1);
                for (String obj : courseObj.keySet()) {
                    CourseVO vo = courses.stream().filter(n -> n.getCsCode().equals(obj)).findFirst().get();
                    List<HotpleVO> infos = new ArrayList<>();
                    hotpleArr = courseObj.getJSONArray(obj);
                    int arrs[] = new int[hotpleArr.length()];
                    for (int i = 0; i < hotpleArr.length(); i++) {
                        arrs[i] = hotpleArr.getInt(i);
                    }
                    hotples.stream().filter(n -> isMatch(n, arrs)).forEach(infos::add);
                    filteredCourse.put(vo.getCsCode(), infos);
                }

                if (uCode != null) {
                    JSONArray hotpleMArr = array.getJSONArray(2);
                    int arrm[] = new int[hotpleMArr.length()];
                    for (int i = 0; i < hotpleMArr.length(); i++) {
                        arrm[i] = hotpleMArr.getInt(i);
                    }
                    hotples.stream().filter(n -> isMatch(n, arrm)).forEach(filteredHotpleM::add);
//                    log.info(filteredHotpleM);

                    org.json.JSONObject courseMObj = array.getJSONObject(3);
                    for (String obj : courseMObj.keySet()) {
                        CourseVO vo = courses.stream().filter(n -> n.getCsCode().equals(obj)).findFirst().get();
                        List<HotpleVO> infos = new ArrayList<>();
                        hotpleMArr = courseMObj.getJSONArray(obj);
                        int arrms[] = new int[hotpleMArr.length()];
                        for (int i = 0; i < hotpleMArr.length(); i++) {
                            arrms[i] = hotpleMArr.getInt(i);
                        }
                        hotples.stream().filter(n -> isMatch(n, arrms)).forEach(infos::add);
                        filteredCourseM.put(vo.getCsCode(), infos);
                    }
                }
            } else {
                log.info(conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.put("hotples", filteredHotple);
        obj.put("courses", filteredCourse);
        if (uCode != null) {
            obj.put("hotplesM", filteredHotpleM);
            obj.put("coursesM", filteredCourseM);
        }
        return obj.toString();
    }

    public boolean isMatch(HotpleVO vo, int arr[]) {
        for (int a : arr) {
            if (vo.getHtId() == a) return true;
        }
        return false;
    }

    @PostMapping("/course_cn")
    public String getCourseCN(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("courses", course.getAllCourse(request.getParameter("uCode")));
        return jsonObject.toString();
    }

    @PostMapping("/get_course")
    public String getCourse(HttpServletRequest request) {
        String csCode = request.getParameter("csCode");
        JSONObject object = new JSONObject();
        object.put("course_info", course.getCourseInfoDetail(csCode));
        return object.toString();
    }

    @Transactional
    @PostMapping("/cancel_reservation")
    public String cancelReserv(HttpServletRequest request) throws Exception {
        ReservationInfoVO ri = reservation.getByCode(request.getParameter("riCode"));

        HttpURLConnection conn = null;
        String access_token = null; // 발급받을 엑세스 토큰
        URL url = new URL("https://api.iamport.kr/users/getToken"); // 액세스 토큰 받을 주소 입력

        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");  // post 방식으로 요청

        // 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

        // 키 설정
        org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
        obj.put("imp_key", "3158229450476427"); // 고유 키
        obj.put("imp_secret", "0ZhM3lMpwifNyac3fGOR92EXeV26EAyEAPrDbd3Hwiu4tH8JnWAwZetdawjP7RtHHVlS9oAFH4KaLTT9");  // 시크릿 키

        // JSON 화한 키 값들 전달하기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(obj.toString());
        bw.flush();
        bw.close();

        int responseCode = conn.getResponseCode();
        log.info("응답 코드 : " + responseCode);

        if (responseCode == 200) {  // 성공
            log.info("토큰 얻기 성공");
            // 버퍼 리더로 반환 값 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            // 차례로 읽기
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            log.info("" + sb.toString());

            // 반환 받은 JSON String 파싱
            JSONParser parser = new JSONParser();
            Map<String, Object> map = (Map<String, Object>) parser.parse(sb.toString());
            Map<String, String> response_map = (Map<String, String>) parser.parse(map.get("response").toString());
            access_token = response_map.get("access_token").toString();
        } else {
            log.info(conn.getResponseMessage());
            return "{message: '아임포트 API 문제가 발생하였습니다.', status:500}";
        }

        log.info("access_token : " + access_token);

        // 환불 요청 페이지로 변경
        conn = null;
        url = new URL("https://api.iamport.kr/payments/cancel");
        conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");  // post 설정

        // 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", access_token); // 앞서 받은 엑세스 토큰 설정

        conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

        // JSON 설정
        obj = new org.json.simple.JSONObject();
        obj.put("merchant_uid", ri.getRiOdNum()); // 내 디비의 merchant_uid 얻기
        obj.put("amount", reservation.getTotalFee(ri.getRiCode())); // 내 디비의 총 메뉴 가격
        obj.put("reason", "운영자의 예약 취소");

        if (!reservation.removeRes(ri.getRiCode())) {
            return "{message: '다시 시도해주십시오.', status: 500}";
        }

        // 요청 보내기
        bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(obj.toString());
        bw.flush();
        bw.close();

        responseCode = conn.getResponseCode();
        log.info("응답 코드 : " + responseCode);

        if (responseCode == 200) {
            log.info("환불 성공");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            log.info("환불 성공 : " + sb.toString());
        } else {
            log.info(conn.getResponseMessage());
            throw new Exception("환불에 실패하였습니다.");
        }

        return "{message: '예약 취소 완료하였습니다.', status: 200}";
    }
}
