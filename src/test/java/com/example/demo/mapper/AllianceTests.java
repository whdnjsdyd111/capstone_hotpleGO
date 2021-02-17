package com.example.demo.mapper;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.service.web.AllianceService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class AllianceTests {
    @Setter(onMethod_ = @Autowired)
   private AllianceService service;

    @Test
    public void getY() {
        service.getListY().forEach(vo -> log.info(vo.getAlcCode() + ", " + vo.getName() + ", " +
                vo.getEmail() + ", " + vo.getPhone() + ", " + vo.getContent()));
    }

    @Test
    public void insertTests() {
        AllianceVO vo = new AllianceVO();
        vo.setContent("매퍼로 제휴 신청");
        vo.setAlcCode(null);
        vo.setEmail("whdnjsdyd111@naver.com");
        vo.setName("조원용");
        vo.setPhone("01068480083");

        service.register(vo);
    }

    @Test
    public void updateTests() {
        service.change("210217010255/32/n");
    }
}
