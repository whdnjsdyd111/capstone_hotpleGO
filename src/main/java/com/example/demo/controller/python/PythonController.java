package com.example.demo.controller.python;

import com.example.demo.domain.CourseWithMbtiVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/python/**")
@RequiredArgsConstructor
@Log4j2
public class PythonController {
    private final UserService user;
    private final CourseService course;

    @GetMapping("/mbti_course")
    public String mbti(@AuthenticationPrincipal CustomUser user) throws Exception {
        String mbti = this.user.getMbti(user.user.getUCode());
        if (mbti == null) return "redirect:/mbti";  // mbti 없으면 mbti 설정 페이지로 리다이렉트

        log.info(mbti);

        List<CourseWithMbtiVO> list = course.getByMbti(mbti);

        log.info(list);

        String csCode = null;   // 얘는 코스 번호
        HttpURLConnection conn = null;
        URL url = new URL("http://127.0.0.1:5000/mbti"); // 액세스 토큰 받을 주소 입력

        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");  // post 방식으로 요청

        // 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

        // 키 설정
        JSONObject obj = new JSONObject();
        obj.put("mbti", JSONArray.toJSONString(list)); // mbti 에 대한 리스트 보내기

        // JSON 화한 키 값들 전달하기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(obj.toString());
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
                sb.append(line + "\n");
            }
            br.close();
            log.info("" + sb.toString());

//            // 반환 받은 JSON String 파싱
//            JSONParser parser = new JSONParser();
//            Map<String, Object> map = (Map<String, Object>) parser.parse(sb.toString());
//            Map<String, String> response_map = (Map<String, String>) parser.parse(map.get("response").toString());
        } else {
            log.info(conn.getResponseMessage());
            return "fail";
        }


        return "success";
    }
}
