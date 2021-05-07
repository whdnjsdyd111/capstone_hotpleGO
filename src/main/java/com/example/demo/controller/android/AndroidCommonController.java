package com.example.demo.controller.android;

import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.ManagerVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.service.CourseService;
import com.example.demo.service.ImageAttachService;
import com.example.demo.service.TasteService;
import com.example.demo.service.UserService;
import com.example.demo.service.web.BoardService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/android/*")
@RequiredArgsConstructor
@Log4j2
public class AndroidCommonController {
    private final PasswordEncoder passwordEncoder;
    private final UserService users;
    private final TasteService taste;
    private final BoardService board;
    private final ImageAttachService imageAttach;
    private final CourseService course;

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
        if (uCode != null) jsonObject.put("bookmark", board.getBookmark(bdCode, uCode));
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
}
