package com.example.demo.controller.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.service.web.AllianceService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/*")
public class HomeRestController {
    @Setter(onMethod_ = @Autowired)
    AllianceService alliance;

    @PostMapping("/alliance")
    public ResponseEntity<String> allianceRegister(AllianceVO vo) {
        return alliance.register(vo) ? new ResponseEntity<>("제휴 감사합니다.", HttpStatus.OK)
                : new ResponseEntity<>("다시시도해 주십시오.", HttpStatus.BAD_REQUEST);
    }
}
