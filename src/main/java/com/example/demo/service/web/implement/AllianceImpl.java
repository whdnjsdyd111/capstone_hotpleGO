package com.example.demo.service.web.implement;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.mapper.web.AllianceMapper;
import com.example.demo.service.web.AllianceService;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AllianceImpl implements AllianceService {
    @Setter(onMethod_ = @Autowired)
    private AllianceMapper mapper;

    @Override
    public AllianceVO get(String code) {
        return mapper.read(code);
    }

    @Override
    public List<AllianceVO> getListN() {
        return mapper.getList('N');
    }

    @Override
    public List<AllianceVO> getListY() {
        return mapper.getList('Y');
    }

    @Transactional
    @Override
    public boolean register(AllianceVO vo) {
        return mapper.insert(vo) == 1;
    }

    @Transactional
    @Override
    public boolean change(String code) {
        return mapper.update(code) == 1;
    }

    @Transactional
    @Override
    public boolean remove(String code) {
        return mapper.delete(code) == 1;
    }
}
