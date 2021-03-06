package com.example.demo.controller.python;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.CourseVO;
import com.example.demo.domain.CourseWithMbtiVO;
import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.UserVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.CourseService;
import com.example.demo.service.HotpleService;
import com.example.demo.service.TasteService;
import com.example.demo.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Controller
@RequestMapping("/python/**")
@RequiredArgsConstructor
@Log4j2
public class PythonController {
    private final UserService user;
    private final CourseService course;
    private final HotpleService hotple;
    private final TasteService taste;

    @Transactional
    @GetMapping("/mbti_course")
    public String mbti(HttpSession session) throws Exception {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        String mbti = this.user.getMbti(vo1.getUCode());
        if (mbti == null) return "redirect:/mbti";  // mbti 없으면 mbti 설정 페이지로 리다이렉트

        log.info(mbti);

        List<CourseWithMbtiVO> list = course.getByMbti(mbti);
        Set<Long> htIds = new HashSet<>();
        list.forEach(l -> {
            htIds.add(l.getHtId());
        });
        List<HotpleVO> hotples = hotple.getHotples(htIds);

        log.info(list);

        String htId[] = null;   // 파이썬으로부터 받을 ht 아이디들
        CourseVO vo = new CourseVO();
        vo.setCsTitle(mbti);
        vo.setCsWith("혼자");
        vo.setCsNum((byte) 1);
        vo.setUCode(vo1.getUCode());

        HttpURLConnection conn = null;
        URL url = new URL("http://127.0.0.1:5000/mbti"); // 액세스 토큰 받을 주소 입력

        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");  // post 방식으로 요청

        // 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정
        // 키 설정
        String mbti_json = new Gson().toJson(list);
        String hotples_json = new Gson().toJson(hotples);

        // JSON 화한 키 값들 전달하기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write("{" + mbti_json + "," + hotples_json + "}");   //json 객체로 1차적으로 다듬어서 보냄.
//        bw.write(mbti_json);
//        bw.newLine();
//        bw.write(hotples_json);
//        bw.write(mbti_json + hotples_json);
        bw.flush();
        bw.close();


        int responseCode = conn.getResponseCode();
        log.info("응답 코드 : " + responseCode);

        if (responseCode == 200) {  // 성공
            log.info("mbti 코스 받기 성공");
            // 버퍼 리더로 반환 값 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            // 차례로 읽기
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            log.info("" + sb.toString());

            String str = sb.toString();
            str = str.replace("[", "").replace("]", "").replaceAll("\"", "");
            htId = str.split(",");
            log.info(htId);
            course.registerMBTI(mbti, vo);
            course.addCourses(htId, vo.getCsCode());
        } else {
            log.info(conn.getResponseMessage());
            return "/";
        }


        return "redirect:/courseDetail/" + HotpleAPI.codeToStr(vo.getCsCode());
    }

    @Transactional
    @PostMapping("/ai_course")
    public String aiCourse(HttpServletRequest request, HttpSession session) throws Exception {
        UserVO vo1 = (UserVO) session.getAttribute("users");

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));
        int area = Integer.parseInt(request.getParameter("area"));


        List<HotpleVO> hotples = hotple.getByGeoAndArea(lat, lng, area);
        int[] tastes = taste.getTasteList(vo1.getUCode()).stream().mapToInt(Integer::valueOf).toArray();
        int[] selected = Arrays.stream(request.getParameter("index").split("")).mapToInt(Integer::parseInt).toArray();
        log.info(area);
        log.info(lat);
        log.info(lng);
        log.info(hotples);
        String htId[] = null;   // 파이썬으로부터 받을 ht 아이디들
        CourseVO vo = new CourseVO();
        vo.setCsTitle("aiCourse");
        vo.setCsWith(request.getParameter("with"));
        vo.setCsNum(Byte.parseByte(request.getParameter("withNum")));
        vo.setUCode(vo1.getUCode());

        HttpURLConnection conn = null;
        URL url = new URL("http://127.0.0.1:5000/course"); // 액세스 토큰 받을 주소 입력

        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");  // post 방식으로 요청

        // 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정
        // 키 설정
        String hotples_json = new Gson().toJson(hotples);
        String tastes_json = new Gson().toJson(tastes);
        String selected_json = new Gson().toJson(selected);

        // JSON 화한 키 값들 전달하기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write("{" + hotples_json + "," + tastes_json + "," + selected_json + "}");   //json 객체로 1차적으로 다듬어서 보냄.
        bw.flush();
        bw.close();

        int responseCode = conn.getResponseCode();
        log.info("응답 코드 : " + responseCode);

        if (responseCode == 200) {  // 성공
            log.info("ai 코스 받기 성공");
            // 버퍼 리더로 반환 값 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            // 차례로 읽기
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            log.info("" + sb.toString());

            String str = sb.toString();
            str = str.replace("[", "").replace("]", "").replaceAll("\"", "");
            htId = str.split(",");
            log.info(htId);
            course.registerAi("aiCourse", vo);
            course.addCourses(htId, vo.getCsCode());
        } else {
            log.info(conn.getResponseMessage());
            return "/";
        }

        return "redirect:/courseDetail/" + HotpleAPI.codeToStr(vo.getCsCode());
    }
}
