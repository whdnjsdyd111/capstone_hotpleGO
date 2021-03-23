package com.example.demo.service.web;

import com.example.demo.domain.web.FeedbackVO;
import com.example.demo.mapper.web.FeedbackMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class FeedbackService {
    @Setter(onMethod_ = @Autowired)
    private FeedbackMapper mapper;

    public FeedbackVO get(String code) {
        return mapper.read(code);
    }

    public List<FeedbackVO> getListN() {
        return mapper.getList("N");
    }

    public List<FeedbackVO> getListY() {
        return mapper.getList("Y");
    }

    @Transactional
    public boolean register(FeedbackVO vo) {
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
