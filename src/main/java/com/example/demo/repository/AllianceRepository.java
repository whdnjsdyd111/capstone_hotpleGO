package com.example.demo.repository;

import com.example.demo.domain.AllianceVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AllianceRepository extends JpaRepository<AllianceVO, String> {
    List<AllianceVO> findAllByAlcCodeEndsWith(String pro);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO alliance VALUES (TO_CHAR(CURRENT_TIMESTAMP, 'RRMMDDHHmmss/') || LPAD(seq_alc.NEXTVAL, 2, '0') || '/N'," +
            " :#{#vo.name}, :#{#vo.email}, :#{#vo.phone}, :#{#vo.content})", nativeQuery = true)
    void insertAlliance(AllianceVO vo);
}
