package com.example.demo.service.implement;

import com.example.demo.domain.AllianceVO;
import com.example.demo.repository.AllianceRepository;
import com.example.demo.service.AllianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AllianceImpl implements AllianceService {
    @Autowired
    private AllianceRepository repository;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * INSERT 쿼리문
     * 기본키 - 년월일시분초 / 시퀀스 / N or Y
     */
    private String insert = "INSERT INTO alliance VALUES (TO_CHAR(CURRENT_TIMESTAMP, 'RRMMDDHHmmss/') || " +
            "LPAD(seq_alc.NEXTVAL, 2, '0') || '/N'," +
            " :name, :email, :phone, :content)";

    /**
     * UPDATE 쿼리문
     * 처리 여부 N -> Y
     */
    private String update = "UPDATE alliance SET alc_code = REPLACE(alc_code, 'N', 'Y') WHERE alc_code = ?";

    @Transactional
    @Override
    public boolean register(AllianceVO vo) {
        return entityManager.createNativeQuery(insert)
                .setParameter("name", vo.getName())
                .setParameter("email", vo.getEmail())
                .setParameter("phone", vo.getPhone())
                .setParameter("content", vo.getContent())
                .executeUpdate() == 1;
    }

    @Override
    public List<AllianceVO> getListN() {
        return repository.findAllByAlcCodeEndsWith("N");
    }

    @Override
    public List<AllianceVO> getListY() {
        return repository.findAllByAlcCodeEndsWith("Y");
    }

    @Override
    public AllianceVO get(String code) {
        Optional<AllianceVO> opt = repository.findById(code);
        return opt.orElse(null);
    }

    @Transactional
    @Override
    public boolean change(String code) {
        return entityManager.createNativeQuery(update)
                .setParameter(1, code)
                .executeUpdate() == 1;
    }
}
