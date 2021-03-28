package com.example.demo.service;

import com.example.demo.domain.ReviewVO;
import com.example.demo.mapper.HotpleMapper;
import com.example.demo.mapper.ReviewMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Setter(onMethod_ = @Autowired)
    ReviewMapper mapper;

    public List<ReviewVO> getList(long htId) {
        return mapper.getList(htId);
    }
}
