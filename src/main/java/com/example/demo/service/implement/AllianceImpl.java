package com.example.demo.service.implement;

import com.example.demo.domain.AllianceVO;
import com.example.demo.repository.AllianceRepository;
import com.example.demo.service.AllianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllianceImpl implements AllianceService {
    @Autowired
    private AllianceRepository repository;

    @Override
    public int register(AllianceVO vo) {
        return 0;
    }

    @Override
    public List<AllianceVO> getListN() {
        return null;
    }

    @Override
    public List<AllianceVO> getListY() {
        return null;
    }
}
