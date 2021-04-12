package com.example.demo.controller.android;

import com.example.demo.domain.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/android/*")
@Log4j2
public class AndroidCommonController {

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody UserVO vo) {
        log.info(vo);
        return null;
    }
}
