package com.example.demo.jpa;

import com.example.demo.domain.AllianceVO;
import com.example.demo.repository.AllianceRepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Log
public class AllianceTests {
    @Autowired
    AllianceRepository repository;

    @Test
    public void insertTests() {
        List<AllianceVO> list = repository.findAll();
        list.forEach(vo -> log.info(vo.toString()));
    }
}
