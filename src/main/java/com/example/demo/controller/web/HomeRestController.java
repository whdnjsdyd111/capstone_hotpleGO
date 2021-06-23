package com.example.demo.controller.web;

import com.example.demo.api.HotpleAPI;
import com.example.demo.domain.*;
import com.example.demo.domain.web.AllianceVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import com.example.demo.service.web.AllianceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.apache.tomcat.jni.Local;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/getWeather")
    public String getWeather() throws IOException {
        String now = LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String yearAndDate = now.substring(0,8);
        String hours = now.substring(8,10);
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        /*HashMap<String, Object> result = new HashMap<>();
        String jsonInString = "";


        String url = "/1360000/VilageFcstInfoService/getUltraSrtNcst";

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(factory);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", "application/json");
            HttpEntity<?> entity = new HttpEntity<>(header);
            UriComponents uri =  UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("apis.data.go.kr")
                    .path(url)
                    .queryParam("ServiceKey","q9bGH%2Bb6Tm1UUbZ9K0EBIllJ64HC2RlamBoq%2FbTXAYTyG06JFGhTBgwtkPNhlZV9bOkOREd5l073KipQPKD0Nw%3D%3D")
                    .queryParam("pageNo","1")
                    .queryParam("numOfRows","10")
                    .queryParam("dataType","JSON")
                    .queryParam("base_date",yearAndDate)
                    .queryParam("base_time",hours + "00")
                    .queryParam("nx","89")
                    .queryParam("ny","91")

                    .build();
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
            result.put("statusCode", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());
            System.out.println(uri);
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return jsonInString;*/
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst"); //URL
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=q9bGH%2Bb6Tm1UUbZ9K0EBIllJ64HC2RlamBoq%2FbTXAYTyG06JFGhTBgwtkPNhlZV9bOkOREd5l073KipQPKD0Nw%3D%3D"); //Service Key
            //urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); //공공데이터포털에서 받은 인증키
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); //페이지번호
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("13", "UTF-8")); //한 페이지 결과 수
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); //요청자료형식(XML/JSON)Default: XML
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yearAndDate, "UTF-8")); // 현재 연월일
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hours + "00", "UTF-8")); // 현재시각 - 2
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("89", "UTF-8")); //예보지점의 X 좌표값
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("91", "UTF-8")); //예보지점 Y 좌표
            URL url = new URL(urlBuilder.toString());


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return sb.toString();

    }

    @PostMapping("/around")
    public ResponseEntity<String> around(HttpServletRequest request, HttpSession session) {
        UserVO userVO = (UserVO) session.getAttribute("users");
        String urlOnOff = userVO == null ? "off" : "on";
        List<HotpleVO> filteredHotple = new ArrayList<>();
        List<HotpleVO> filteredHotpleM = new ArrayList<>();
        Map<String, List<HotpleVO>> filteredCourse = new HashMap<>();
        Map<String, List<HotpleVO>> filteredCourseM = new HashMap<>();
        String lat = request.getParameter("lat");
        String lng = request.getParameter("lng");

        try {
            HttpURLConnection conn = null;
            URL url = new URL("http://127.0.0.1:5000/" + urlOnOff); // 액세스 토큰 받을 주소 입력

            String mbti = "[{mbti: ";
            if (userVO != null) {
                mbti = this.user.getMbti(userVO.getUCode());
                if (mbti == null) return new ResponseEntity<>("no-mbti", HttpStatus.BAD_GATEWAY);
            }
            mbti += "}]";

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");  // post 방식으로 요청

            // 헤더 설정
            conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

            List<HotpleVO> hotples = hotple.getByGeoAndArea(lat == null ? 35.8992601 : Double.parseDouble(lat),
                    lng == null ? 128.6215081 : Double.parseDouble(lng), 3);
            List<CourseVO> courses = course.getCourses();
            List<CourseInfoVO> courseInfos = course.getCourseInfos();

            // 키 설정
            String hotples_json = new Gson().toJson(hotples);
//            log.info(hotples_json);
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

                if (userVO != null) {
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

        org.json.JSONObject obj = new org.json.JSONObject();
        obj.put("hotples", filteredHotple);
        obj.put("courses", filteredCourse);
        if (userVO != null) {
            obj.put("hotplesM", filteredHotpleM);
            obj.put("coursesM", filteredCourseM);
        }
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
    public ResponseEntity<String> settingNick(@RequestBody UserVO vo, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        if (this.user.updateNick(vo.getNick(), vo1.getUCode())) {
            vo1.setNick(vo.getNick());
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
    public ResponseEntity<String> saveMBTI(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        log.info(request.getParameter("mbti"));
        if (this.user.updateMbti(request.getParameter("mbti"), vo.getUCode())) {
            return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save-taste")
    @ResponseBody
    public ResponseEntity<String> saveTaste(@RequestParam(value = "tastes[]") List<Integer> tastes,
                                            HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String code = vo.getUCode();
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
        String keyword = request.getParameter("keyword");
        List<HotpleVO> list = hotple.getByKeywordGeo(keyword, lat, lng);
        log.info("리스트 사이즈 : " + list.size());

        if (list.size() < 5) {
            long category = hotple.searchGoogle(keyword);
            log.info(category);
            try {
                HttpURLConnection conn = null;
                URL url = new URL("http://127.0.0.1:5000/select"); // 액세스 토큰 받을 주소 입력

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");  // post 방식으로 요청

                // 헤더 설정
                conn.setRequestProperty("Content-Type", "application/json");    // 서버에서 받을 Data 방식 설정
                conn.setRequestProperty("Accept", "application/json");

                conn.setDoOutput(true); // OutputStream 으로 POST 데이터를 넘겨주겠다는 설정

                // JSON 화한 키 값들 전달하기
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                org.json.JSONObject object = new org.json.JSONObject();
                object.put("latitude", String.valueOf(lat));
                object.put("longitude", String.valueOf(lng));
                object.put("keyword", keyword);
                bw.write(object.toString());   //json 객체로 1차적으로 다듬어서 보냄.

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

                    List<HotpleVO> newList = new ArrayList<>();

                    // newList 디비에 삽입

                    long htId = hotple.getHtId();
                    log.info(hotple.getHtId());

                    JSONArray arr = new JSONArray(str);
                    for (int i = 0; i < arr.length(); i++) {
                        org.json.JSONObject obj = arr.getJSONObject(i);
                        HotpleVO vo = new HotpleVO();
                        if (hotple.readGoId(obj.getString("GOID")) == null) {
                            vo.setGoId(obj.getString("GOID"));
                            vo.setBusnName(obj.getString("BUSNNAME"));
                            vo.setGoGrd(obj.getDouble("GOGRD"));
                            vo.setGoImg(obj.getString("GOIMG"));
                            vo.setHtAddr(obj.getString("HTADDR"));
                            vo.setHtLng(obj.getDouble("HTLNG"));
                            vo.setHtLat(obj.getDouble("HTLAT"));
                            vo.setHtTel(obj.getString("HTTEL"));
                            vo.setCategory(category);
                            newList.add(vo);
                            System.out.println(vo);
                        }
                    }
                    list.addAll(newList);
                    for (int i = 1; i <= newList.size(); i++) {
                        newList.get(i - 1).setHtId(htId + i);
                    }
                    if (newList.size() != 0) hotple.insertGoogle(newList);
                } else {
                    log.info(conn.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/submit-review")
    public ResponseEntity<String> submitReview(@RequestBody ReviewVO vo, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        vo.setUCode(vo1.getUCode());
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
    public ResponseEntity<String> customCourse(@RequestBody CourseVO vo, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        vo.setUCode(vo1.getUCode());
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
                                              @RequestParam("riCont") String riCont, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        ReservationInfoVO ri = ReservationInfoVO.builder().riPerson(riPerson)
                .riOdNum(riOdNum).riCont(riCont).uCode(vo.getUCode())
                .riTime(Timestamp.valueOf(riTime)).htId(Long.parseLong(meCode.get(0).split("/")[0])).build();
        log.info(ri);
        if (reservation.registerRes(ri)) {
            log.info(ri);
            List<ReservationStatusVO> list = new ArrayList<>();
            for (int i = 0; i < meCode.size(); i++) {
                list.add(ReservationStatusVO.builder().riCode(ri.getRiCode()).meCode(meCode.get(i))
                        .rsMeNum(rsMeNum.get(i)).uCode(vo.getUCode()).build());
            }
            if (reservation.registerResStatus(list)) {
                return new ResponseEntity<>("예약을 완료하였습니다.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("예약에 실패하였습니다. 환불이 진행됩니다.", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/refund")
    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<String> refund(HttpServletRequest request, HttpSession session) throws Exception {
        UserVO vo = (UserVO) session.getAttribute("users");
        ReservationInfoVO ri = reservation.getByCode(request.getParameter("riCode"));
        if (!ri.getUCode().equals(vo.getUCode()))
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
        obj.put("reason", "환불 요청");

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
    public ResponseEntity<String> checkCourse(HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        if (course.checkUsing(vo.getUCode())) {
            return new ResponseEntity<>("Y", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("N", HttpStatus.OK);
        }
    }

    @PostMapping("/change-course")
    public ResponseEntity<String> changeCourse(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        course.changeCourse(vo.getUCode(), request.getParameter("csCode"));
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

    @Transactional
    @PostMapping("/course-copy")
    public ResponseEntity<String> copyCourse(HttpServletRequest request, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        String csCode = request.getParameter("csCode");
        String csWith = request.getParameter("csWith");
        String csNum = request.getParameter("csNum");
        String csTitle = request.getParameter("csTitle");

        CourseVO vo = new CourseVO();
        vo.setCsWith(csWith);
        vo.setCsNum(Byte.valueOf(csNum));
        vo.setCsTitle(csTitle);
        vo.setUCode(vo1.getUCode());

        if (course.register(vo)) {
            if (course.copyCourse(vo.getCsCode(), csCode) > 0) {
                return new ResponseEntity<>(vo.getCsCode(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("다시 시도해주십시오", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<String> insertGuide(HttpServletRequest request, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        GuideApplyVO vo = new GuideApplyVO();
        vo.setUCode(vo1.getUCode());
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
    public ResponseEntity<String> pickHotple(HttpServletRequest request, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        PickListVO vo = new PickListVO();
        vo.setUCode(vo1.getUCode());
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
    public ResponseEntity<String> pickCourse(HttpServletRequest request, HttpSession session) {
        UserVO vo1 = (UserVO) session.getAttribute("users");
        PickListVO vo = new PickListVO();
        vo.setUCode(vo1.getUCode());
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
    public ResponseEntity<String> deletePickHotple(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String uCode = vo.getUCode();
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
    public ResponseEntity<String> deletePickCourse(HttpServletRequest request, HttpSession session) {
        UserVO vo = (UserVO) session.getAttribute("users");
        String uCode = vo.getUCode();
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
