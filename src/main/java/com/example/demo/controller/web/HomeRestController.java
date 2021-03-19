package com.example.demo.controller.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.service.web.AllianceService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/*")
@Log4j2
public class HomeRestController {
    @Setter(onMethod_ = @Autowired)
    AllianceService alliance;

    @PostMapping("/alliance")
    public ResponseEntity<String> allianceRegister(@RequestBody AllianceVO vo) {
        if (vo.getName().isEmpty() || vo.getEmail().isEmpty() || vo.getPhone().isEmpty() || vo.getContent().isEmpty()) {
            return new ResponseEntity<>("모두 빠짐없이 작성해 주십시오.", HttpStatus.BAD_REQUEST);
        }
        return alliance.register(vo) ? new ResponseEntity<>("제휴 감사합니다.", HttpStatus.OK)
                : new ResponseEntity<>("다시 시도 해주십시오.", HttpStatus.BAD_REQUEST);
    }
}
