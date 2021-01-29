package com.example.demo.repository;

import com.example.demo.domain.Visit_ViewVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Visit_ViewRepository extends JpaRepository<Visit_ViewVO, String> {
    List<Visit_ViewVO> findAllByVisitDateEndsWith(String pro);
}
