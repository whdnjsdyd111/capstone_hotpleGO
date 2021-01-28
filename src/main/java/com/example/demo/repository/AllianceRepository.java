package com.example.demo.repository;

import com.example.demo.domain.AllianceVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllianceRepository extends JpaRepository<AllianceVO, String> {
    List<AllianceVO> findAllByAlcCodeEndsWith(String pro);
}
