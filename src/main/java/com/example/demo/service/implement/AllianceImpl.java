package com.example.demo.service.implement;

import com.example.demo.domain.AllianceVO;
import com.example.demo.repository.AllianceRepository;
import com.example.demo.service.AllianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllianceImpl implements AllianceService {
    @Autowired
    private AllianceRepository repository;

    @Override
    public void register(AllianceVO vo) {
        repository.saveAndFlush(vo);
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
}
