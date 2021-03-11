package com.example.demo.service.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.mapper.web.AllianceMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class AllianceService {
    @Setter(onMethod_ = @Autowired)
    private AllianceMapper mapper;

    public AllianceVO get(String code) {
        return mapper.read(code);
    }

    public List<AllianceVO> getListN() {
        return mapper.getList("N");
    }

    public List<AllianceVO> getListY() {
        return mapper.getList("Y");
    }

    @Transactional
    public boolean register(AllianceVO vo) {
        return mapper.insert(vo) == 1;
    }

    @Transactional
    public boolean change(String code) {
        return mapper.update(code) == 1;
    }

    @Transactional
    public boolean remove(String code) {
        return mapper.delete(code) == 1;
    }
}
