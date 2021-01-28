package com.example.demo.controller.web;

import com.example.demo.domain.AllianceVO;
import com.example.demo.service.implement.AllianceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeRestController {
    @Autowired
    AllianceImpl service;

    @PostMapping("/register")
    public ResponseEntity<String> allianceRegister(AllianceVO vo) {
        return service.register(vo) ? new ResponseEntity<>("제휴 감사합니다.", HttpStatus.OK)
                : new ResponseEntity<>("다시시도해 주십시오.", HttpStatus.BAD_REQUEST);
    }
}
