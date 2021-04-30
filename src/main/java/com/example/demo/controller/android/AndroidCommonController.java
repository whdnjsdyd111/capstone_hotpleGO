package com.example.demo.controller.android;

import com.example.demo.domain.ManagerVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.service.TasteService;
import com.example.demo.service.UserService;
import com.example.demo.service.web.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    // 유저 정보

    @PostMapping("/login")
    public String login(HttpServletRequest request) throws JSONException {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        UserVO vo = users.getByEmail("id");
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("board", new JSONObject(board.getBoardDetail(bdCode)));
        return jsonObject.toString();
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


}
