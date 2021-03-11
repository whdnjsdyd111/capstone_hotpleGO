package com.example.demo.controller.web;

import com.example.demo.domain.HotpleVO;
import com.example.demo.service.HotpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager/rest/*")
@RequiredArgsConstructor
@Log4j2
public class ManagerRestController {
    private final HotpleService hotple;

    @PostMapping("/comp-erm")
    public ResponseEntity<String> companyEnrollment(@RequestBody HotpleVO vo) {
        Long htId = hotple.getIdByAddr(vo.getHtAddr());
        if (htId == null) {
            if (hotple.registerBusn(vo)) {
                return new ResponseEntity<>("등록 완료하였습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("등록 실패하였습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {

        }

        return new ResponseEntity<>("ㅇㅇ", HttpStatus.OK);
    }
}
