package com.example.demo.controller.web;

import com.example.demo.domain.ManagerVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.AllianceVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.UserService;
import com.example.demo.service.web.AllianceService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/*")
@RequiredArgsConstructor
@Log4j2
public class HomeRestController {
    private final AllianceService alliance;
    private final UserService user;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/alliance")
    public ResponseEntity<String> allianceRegister(@RequestBody AllianceVO vo) {
        if (vo.getName().isEmpty() || vo.getEmail().isEmpty() || vo.getPhone().isEmpty() || vo.getContent().isEmpty()) {
            return new ResponseEntity<>("모두 빠짐없이 작성해 주십시오.", HttpStatus.BAD_REQUEST);
        }
        return alliance.register(vo) ? new ResponseEntity<>("제휴 감사합니다.", HttpStatus.OK)
                : new ResponseEntity<>("다시 시도 해주십시오.", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/setting-nick")
    public ResponseEntity<String> settingNick(@RequestBody UserVO vo) {
        // TODO
        if (user.updateNick(vo.getNick(), "whdnjsdyd1112@naver.com/U/GO")) {
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
    public ResponseEntity<String> saveMBTI(HttpServletRequest request) {
        // TODO
        log.info(request.getParameter("mbti"));
        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }
}
