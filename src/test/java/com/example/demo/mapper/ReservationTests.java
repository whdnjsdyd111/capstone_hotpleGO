package com.example.demo.mapper;

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
public class ReservationTests {
    @Setter(onMethod_ = @Autowired)
    private ReservationMapper mapper;

    @Test
    public void reservations() {
//        log.info(mapper.getReservation(5L));
    }
}
