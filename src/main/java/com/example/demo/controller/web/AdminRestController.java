package com.example.demo.controller.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.service.web.implement.AllianceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/admin/rest/")
@RequiredArgsConstructor
@Log4j2
public class AdminRestController {
    private final AllianceImpl alliance;

    @PostMapping(value= "/changeAlc")
    public ResponseEntity<String> changeAlc(@RequestBody AllianceVO vo) {
        return alliance.change(UriEncoder.decode(vo.getAlcCode())) ? new ResponseEntity<>("성공적으로 처리하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("다시 시도해주십시요.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteAlc")
    public ResponseEntity<String> deleteAlc(@RequestBody AllianceVO vo) {
        return alliance.remove(UriEncoder.decode(vo.getAlcCode())) ? new ResponseEntity<>("성공적으로 삭제하였습니다.", HttpStatus.OK) :
                new ResponseEntity<>("다시 시도해주십시요.", HttpStatus.BAD_REQUEST);
    }
}
