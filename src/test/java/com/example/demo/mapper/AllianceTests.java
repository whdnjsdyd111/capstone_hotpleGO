package com.example.demo.mapper;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Log
public class AllianceTests {
    @Autowired
    private AllianceMapper mapper;

    @Test
    public void listTests() {
        mapper.findAll().forEach(vo -> log.info(vo.toString()));
    }
}
