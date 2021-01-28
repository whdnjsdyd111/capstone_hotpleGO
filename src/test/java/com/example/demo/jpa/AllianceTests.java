package com.example.demo.jpa;

import com.example.demo.entity.AllianceVO;
import com.example.demo.repository.AllianceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AllianceTests {
    @Autowired
    AllianceRepository repository;

    @Test
    public void insertTests() {
        AllianceVO vo = new AllianceVO();
        vo.setContent("제휴 신청합니다!");
        vo.setEmail("whdnjsdyd111@naver.com");
        vo.setPhone("01068480083");
        vo.setName("조원용");
        vo.setAlc_code("210128111950/00/n");
        repository.saveAndFlush(vo);
    }
}
