package com.example.demo.jpa;

import com.example.demo.domain.AllianceVO;
import com.example.demo.repository.AllianceRepository;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Log4j2
public class AllianceTests {
    @Autowired
    AllianceRepository repository;

    @Test
    public void insertTests() {
        log.info(repository.findAllByAlcCodeEndsWith("Y"));
    }
}
