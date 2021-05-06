package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.domain.web.AdminVO;
import com.example.demo.mapper.UserMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Log4j2
public class UserService {
    @Setter(onMethod_ = @Autowired)
    UserMapper userMapper;

    public List<UserVO> getList() {
        return userMapper.userList();
    }

    public UserVO get(String code) {
        return userMapper.read(code);
    }

    public UserVO getByEmail(String email) {
        return userMapper.readByEmail(email);
    }

    public boolean register(UserVO vo) {
        if (!vo.getPw().isEmpty()) {
            vo.setPw(new BCryptPasswordEncoder().encode(vo.getPw()));
        }
        return userMapper.insert(vo) == 1;
    }

    public AdminVO getAdmin(String code) {
        return userMapper.readAdmin(code);
    }

    public ManagerVO getManager(String code) {
        return userMapper.readManager(code);
    }

    public String getPw(String code) {
        return userMapper.readPassword(code);
    }

    public boolean registerManager(ManagerVO vo) {
        if (!vo.getPw().isEmpty()) {
            vo.setPw(new BCryptPasswordEncoder().encode(vo.getPw()));
        }
        return userMapper.insertManager(vo) == 2;
    }

    public boolean updateNick(String nick, String code) {
        return userMapper.updateNick(nick, code) == 1;
    }

    public boolean updateAccount(ManagerVO vo) {
        return userMapper.updateAccount(vo) == 1;
    }

    public boolean updatePw(String pw, String code) {
        return userMapper.updatePw(pw, code) == 1;
    }

    public boolean updateMbti(String mbti, String uCode) {
        return userMapper.updateMbti(mbti, uCode) == 1;
    }

    public String getMbti(String uCode) {
        return userMapper.getMbti(uCode);
    }

    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {

        String api_key = "NCS3LBBRBSBSOBS5";
        String api_secret = "JBDN6LZKLXNOWUOZRMUVLPJ0FE6ADKET";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01081814326");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "인증번호는" + "["+cerNum+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }

    public boolean pickHotple(PickListVO pickListVO) {
        return userMapper.pickHotple(pickListVO) == 1;
    }

    public boolean pickCourse(PickListVO pickListVO) {
        return userMapper.pickCourse(pickListVO) == 1;
    }

    public List<HotpleVO> getPickHotpleList(String uCode) {
        return userMapper.getPickHotpleList(uCode);
    }

    public List<CourseVO> getPickCourseList(String uCode) {
        return userMapper.getPickCourseList(uCode);
    }

    public boolean deletePickHotple(String htId, String uCode) {
        return userMapper.deletePickHotple(htId, uCode) == 1;
    }

    public boolean deletePickCourse(String csCode, String uCode) {
        return userMapper.deletePickCourse(csCode, uCode) == 1;
    }
}
