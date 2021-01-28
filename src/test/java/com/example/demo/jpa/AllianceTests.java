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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Log4j2
public class AllianceTests {
    @Autowired
    AllianceRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void insertTests() {
        entityManager.createNativeQuery("INSERT INTO alliance VALUES (TO_CHAR(CURRENT_TIMESTAMP, 'RRMMDDHHmmss/') || \" +\n" +
                "LPAD(seq_alc.NEXTVAL, 2, '0') || '/N',\" +\n" +
                " :name, :email, :phone, :content)")
                .setParameter("name", "조원용")
                .setParameter("email", "whdnjsdyd111@naver.com")
                .setParameter("phone", "01068480083")
                .setParameter("content", "제휴 신청!")
                .executeUpdate();
    }

    @Test
    public void updateTests() {
        String code = "210128100131/09/N";
        entityManager.createNativeQuery("UPDATE alliance SET alc_code = REPLACE(alc_code, 'N', 'Y') WHERE alc_code = ?")
                .setParameter(1, code).executeUpdate();

    }
}
