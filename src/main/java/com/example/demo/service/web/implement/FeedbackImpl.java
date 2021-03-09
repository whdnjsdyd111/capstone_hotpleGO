package com.example.demo.service.web.implement;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.domain.web.FeedbackVO;
import com.example.demo.mapper.web.AllianceMapper;
import com.example.demo.mapper.web.FeedbackMapper;
import com.example.demo.service.web.AllianceService;
import com.example.demo.service.web.FeedbackService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class FeedbackImpl implements FeedbackService {
    @Setter(onMethod_ = @Autowired)
    private FeedbackMapper mapper;

    @Override
    public FeedbackVO get(String code) {
        return mapper.read(code);
    }

    @Override
    public List<FeedbackVO> getListN() {
        return mapper.getList("N");
    }

    @Override
    public List<FeedbackVO> getListY() {
        return mapper.getList("Y");
    }

    @Transactional
    @Override
    public boolean register(FeedbackVO vo) {
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
