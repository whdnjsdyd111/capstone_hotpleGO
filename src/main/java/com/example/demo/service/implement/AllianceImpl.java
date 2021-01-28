package com.example.demo.service.implement;

import com.example.demo.domain.AllianceVO;
import com.example.demo.mapper.AllianceMapper;
import com.example.demo.service.AllianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllianceImpl implements AllianceService {
    @Autowired
    private AllianceMapper mapper;

    @Override
    public List<AllianceVO> getList() {
        List<AllianceVO> list = mapper.findAll();
        return list;
    }
}
