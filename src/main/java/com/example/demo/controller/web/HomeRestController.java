package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.*;
import com.example.demo.domain.web.AllianceVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.example.demo.service.web.AllianceService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/*")
@RequiredArgsConstructor
@Log4j2
public class HomeRestController {
    private final AllianceService alliance;
    private final UserService user;
    private final TasteService taste;
    private final HotpleService hotple;
    private final ReviewService review;
    private final CourseService course;
    private final ReservationService reservation;
    private final PasswordEncoder passwordEncoder;
    private final GuideService guideService;

    @PostMapping("/around")
    public ResponseEntity<String> around(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        String urlOnOff = user == null ? "off" : "on";
        List<HotpleVO> filteredHotple = new ArrayList<>();
        Map<String, List<HotpleVO>> filteredCourse = new HashMap<>();

        try {
            HttpURLConnection conn = null;
            URL url = new URL("http://127.0.0.1:5000/" + urlOnOff); // 액세스 토큰 받을 주소 입력

            String mbti = "[{mbti: ";
            if (user != null) {
                mbti = this.user.getMbti(user.user.getUCode());
                if (mbti == null) return new ResponseEntity<>("no-mbti", HttpStatus.BAD_GATEWAY);
            }
            mbti += "}]";

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");  // post 방식으로 요청

            // 헤더 설정
            conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

            List<HotpleVO> hotples = hotple.getByGeoAndArea(35.8992601, 128.6215081, 3);
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
                log.info("" + sb.toString());

                String str = sb.toString();
                log.info(str);

                JSONArray array = new JSONArray(str);

                JSONArray hotpleArr = array.getJSONArray(0);
                int arr[] = new int[hotpleArr.length()];
                for (int i = 0; i < hotpleArr.length(); i++) {
                    arr[i] = hotpleArr.getInt(i);
                }
                hotples.stream().filter(n -> isMatch(n, arr)).forEach(filteredHotple::add);
                log.info(filteredHotple);

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
                log.info(filteredCourse);
            } else {
                log.info(conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        org.json.JSONObject obj = new org.json.JSONObject();
        obj.put("hotples", filteredHotple);
        obj.put("courses", filteredCourse);
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }

    public boolean isMatch(HotpleVO vo, int arr[]) {
        for (int a : arr) {
            if (vo.getHtId() == a) return true;
        }
        return false;
    }

    @PostMapping("/alliance")
    public ResponseEntity<String> allianceRegister(@RequestBody AllianceVO vo) {
        if (vo.getName().isEmpty() || vo.getEmail().isEmpty() || vo.getPhone().isEmpty() || vo.getContent().isEmpty()) {
            return new ResponseEntity<>("모두 빠짐없이 작성해 주십시오.", HttpStatus.BAD_REQUEST);
        }
        return alliance.register(vo) ? new ResponseEntity<>("제휴 감사합니다.", HttpStatus.OK)
                : new ResponseEntity<>("다시 시도 해주십시오.", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/setting-nick")
    public ResponseEntity<String> settingNick(@RequestBody UserVO vo, @AuthenticationPrincipal CustomUser user) {
        if (this.user.updateNick(vo.getNick(), user.user.getUCode())) {
            return new ResponseEntity<>("닉네임 변경 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setting-password")
    public ResponseEntity<String> settingPassword(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        String pw = request.getParameter("password");
        String new_pw = request.getParameter("new_password");
        String code = user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/";
        if (new BCryptPasswordEncoder().matches(pw, this.user.getPw(code))) {
            if (this.user.updatePw(passwordEncoder.encode(new_pw), code)) {
                return new ResponseEntity<>("비밀번호 변경 완료하였습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("변경 실패하였습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/save-mbti")
    public ResponseEntity<String> saveMBTI(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        log.info(request.getParameter("mbti"));
        if (this.user.updateMbti(request.getParameter("mbti"), user.user.getUCode())) {
            return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save-taste")
    @ResponseBody
    public ResponseEntity<String> saveTaste(@RequestParam(value = "tastes[]") List<Integer> tastes,
                                            @AuthenticationPrincipal CustomUser user) {
        String code = user.getUsername() + "/" + user.getAuthorities().toArray()[0] + "/";
        taste.reset(code);
        if (taste.registerAll(code, tastes)) {
            return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setGeo")
    public ResponseEntity<List<HotpleVO>> setGeo(HttpServletRequest request) {
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));
        return new ResponseEntity<>(hotple.getByKeywordGeo(request.getParameter("keyword"), lat, lng), HttpStatus.OK);
    }

    @PostMapping("/submit-review")
    public ResponseEntity<String> submitReview(@RequestBody ReviewVO vo, @AuthenticationPrincipal CustomUser user) {
        vo.setUCode(user.user.getUCode());
        vo.setRiCode(HotpleAPI.strToCode(vo.getRiCode()));
        if (review.registerReview(vo)) {
            return new ResponseEntity<>("리뷰 작성 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update-review")
    public ResponseEntity<String> updateReview(@RequestBody ReviewVO vo) {
        vo.setRiCode(HotpleAPI.strToCode(vo.getRiCode()));
        if (review.updateReview(vo)) {
            return new ResponseEntity<>("리뷰를 수정하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/custom-course")
    public ResponseEntity<String> customCourse(@RequestBody CourseVO vo, @AuthenticationPrincipal CustomUser user) {
        vo.setUCode(user.user.getUCode());
        log.info(vo.getCsTitle());
        if (course.register(vo)) {
            return new ResponseEntity<>(vo.getCsCode(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-in-course")
    public ResponseEntity<String> addInCourse(HttpServletRequest request) {
        String csCode = request.getParameter("csCode");
        String htId = request.getParameter("htId");
        if (!course.alreadyAdded(csCode, htId)) {
            if (course.addCourse(csCode, htId)) {
                return new ResponseEntity<>("코스에 추가하였습니다!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("이미 코스에 추가되어 있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reservation-complete")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> resComplete(@RequestParam("meCode[]") List<String> meCode,
                                              @RequestParam("rsMeNum[]") List<Integer> rsMeNum, @RequestParam("riTime") String riTime,
                                              @RequestParam("riPerson") short riPerson, @RequestParam("riOdNum") String riOdNum,
                                              @RequestParam("riCont") String riCont, @AuthenticationPrincipal CustomUser user) {
        ReservationInfoVO ri = ReservationInfoVO.builder().riPerson(riPerson)
                .riOdNum(riOdNum).riCont(riCont).uCode(user.user.getUCode())
                .riTime(Timestamp.valueOf(riTime)).htId(Long.parseLong(meCode.get(0).split("/")[0])).build();
        log.info(ri);
        if (reservation.registerRes(ri)) {
            log.info(ri);
            List<ReservationStatusVO> list = new ArrayList<>();
            for (int i = 0; i < meCode.size(); i++) {
                list.add(ReservationStatusVO.builder().riCode(ri.getRiCode()).meCode(meCode.get(i))
                        .rsMeNum(rsMeNum.get(i)).uCode(user.user.getUCode()).build());
            }
            if (reservation.registerResStatus(list)) {
                return new ResponseEntity<>("예약을 완료하였습니다.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("예약에 실패하였습니다. 환불이 진행됩니다.", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/refund")
    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<String> refund(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) throws Exception {

        ReservationInfoVO ri = reservation.getByCode(request.getParameter("riCode"));
        if (!ri.getUCode().equals(user.user.getUCode()))
            return new ResponseEntity<>("본인의 예약이 아닙니다.", HttpStatus.BAD_REQUEST);

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
        JSONObject obj = new JSONObject();
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
            return new ResponseEntity<>("아임 포트 API 에 문제가 생겼습니다.", HttpStatus.BAD_REQUEST);
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
        obj = new JSONObject();
        obj.put("merchant_uid", ri.getRiOdNum()); // 내 디비의 merchant_uid 얻기
        obj.put("amount", reservation.getTotalFee(ri.getRiCode())); // 내 디비의 총 메뉴 가격
        obj.put("reason", "취소 테스트");

        if (!reservation.removeRes(ri.getRiCode())) {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST); // 먼저 디비부터 삭제
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

        return new ResponseEntity<>("환불 성공하였습니다.", HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> transactionErr(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/delete-hotple-in-course")
    public ResponseEntity<String> deleteHotpleInCourse(HttpServletRequest request) {
        course.removeHtInCs(request.getParameter("csCode"), request.getParameter("htId"));
        return new ResponseEntity<>("성공적으로 삭제하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/delete-course")
    public ResponseEntity<String> deleteCourse(HttpServletRequest request) {
        if (course.removeCourse(request.getParameter("csCode"))) {
            return new ResponseEntity<>("코스를 삭제하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/check-course")
    public ResponseEntity<String> checkCourse(@AuthenticationPrincipal CustomUser user) {
        if (course.checkUsing(user.user.getUCode())) {
            return new ResponseEntity<>("Y", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("N", HttpStatus.OK);
        }
    }

    @PostMapping("/change-course")
    public ResponseEntity<String> changeCourse(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        course.changeCourse(user.user.getUCode(), request.getParameter("csCode"));
        return new ResponseEntity<>("해당 코스로 교체하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/use-course")
    public ResponseEntity<String> useCourse(HttpServletRequest request) {
        course.changeUseCourse(request.getParameter("csCode"));
        return new ResponseEntity<>("해당 코스로 이용에 성공하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/return-course")
    public ResponseEntity<String> returnCourse(HttpServletRequest request) {
        course.returnCourse(request.getParameter("csCode"));
        return new ResponseEntity<>("코스를 내렸습니다.", HttpStatus.OK);
    }

    @PostMapping("/complete-course")
    public ResponseEntity<String> completeCourse(HttpServletRequest request) {
        course.completeCourse(request.getParameter("csCode"));
        return new ResponseEntity<>("코스를 완료하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/reorder-hotple")
    public ResponseEntity<String> reorder(@RequestParam("htId[]") Long[] htId,
                                          @RequestParam("ciIndex[]") Byte[] ciIndex,
                                          @RequestParam("csCode") String csCode) {
        List<CourseInfoVO> list = new ArrayList<>();
        for (int i = 0; i < htId.length; i++) {
            CourseInfoVO vo = new CourseInfoVO();
            vo.setHtId(htId[i]);
            vo.setCiIndex(ciIndex[i]);
            list.add(vo);
        }
        course.updateOrder(list, csCode);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @PostMapping("/setting-guide")
    @ResponseBody
    public ResponseEntity<String> insertGuide(HttpServletRequest request, @AuthenticationPrincipal CustomUser user) {
        GuideApplyVO vo = new GuideApplyVO();
        vo.setUCode(user.user.getUCode());
        vo.setGCont(request.getParameter("gCont"));
        log.info(vo);
        boolean isInserted = guideService.insertGuide(vo);
        try {
            if (isInserted == true) {
                log.info("성공");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("신청 완료", HttpStatus.OK);
    }

    @PostMapping("/pick-hotple")
    public ResponseEntity<String> pickHotple(HttpServletRequest request, @AuthenticationPrincipal CustomUser users) {
        PickListVO vo = new PickListVO();
        vo.setUCode(users.user.getUCode());
        vo.setHtId(request.getParameter("htId"));
        log.info(vo);
        boolean isInserted = user.pickHotple(vo);
        try {
            if (isInserted == true) {
                log.info("성공");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("찜 완료", HttpStatus.OK);
    }

    @PostMapping("/pick-course")
    public ResponseEntity<String> pickCourse(HttpServletRequest request, @AuthenticationPrincipal CustomUser users) {
        PickListVO vo = new PickListVO();
        vo.setUCode(users.user.getUCode());
        vo.setCsCode(request.getParameter("csCode"));
        log.info(vo);
        boolean isInserted = user.pickCourse(vo);
        try {
            if (isInserted == true) {
                log.info("성공");
            }
        } catch (Exception e) {
            log.warn("에러 발생");
        }
        return new ResponseEntity<>("찜 완료", HttpStatus.OK);
    }

    @PostMapping("/pick-delete")
    public ResponseEntity<String> deletePickHotple(HttpServletRequest request, @AuthenticationPrincipal CustomUser users) {

        String uCode = users.user.getUCode();
        String htId = request.getParameter("htId");

        boolean isDeleted = user.deletePickHotple(htId, uCode);
        try {
            if (isDeleted == true) {
                log.info("삭제 완료");
            }
        } catch (Exception e) {
            log.info("에러 발생");
        }
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    @PostMapping("pickCourse-delete")
    public ResponseEntity<String> deletePickCourse(HttpServletRequest request, @AuthenticationPrincipal CustomUser users) {
        String uCode = users.user.getUCode();
        String csCode = request.getParameter("csCode");

        boolean isDeleted = user.deletePickCourse(csCode, uCode);
        try {
            if (isDeleted == true) {
                log.info("삭제 완료");
            }
        } catch (Exception e) {
            log.info("에러 발생");
        }
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }


    @PostMapping("/forgotPw")
    public ResponseEntity<String> forgotPw(HttpServletRequest request, UserVO vo) {
        String email = request.getParameter("uCode");
        String phone = request.getParameter("phone");
        String uCode = vo.getUCode().split("/")[0];

        if (uCode.equals(email)) {
            if (vo.getPhone().equals(phone)) {
                return new ResponseEntity<>("성공", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("실패", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("성공", HttpStatus.OK);
    }

    @PostMapping("/setNewPw")
    public ResponseEntity<String> forgotNewPw(HttpServletRequest request) {
        String new_pw = request.getParameter("newPw");
        String code = request.getParameter("uCode");
        log.info(new_pw);
        log.info(code);
        if (this.user.updatePw(passwordEncoder.encode(new_pw), code)) {
            return new ResponseEntity<>("비밀번호 변경 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("변경 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
