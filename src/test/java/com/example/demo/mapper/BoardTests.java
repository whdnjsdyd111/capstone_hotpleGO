package com.example.demo.mapper;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class BoardTests {
    @Setter(onMethod_ = @Autowired)
    private ReservationMapper mapper;

    @Test
    public void test() {
        log.info(mapper.getByCode("210411214643/15"));
    }
}
