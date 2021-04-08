package com.example.demo.service;

import com.example.demo.domain.ReviewVO;
import com.example.demo.mapper.HotpleMapper;
import com.example.demo.mapper.ReviewMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    @Setter(onMethod_ = @Autowired)
    ReviewMapper mapper;

    public List<ReviewVO> getList(long htId) {
        return mapper.getList(htId);
    }

    public Map<String, ReviewVO> getListByUser(String uCode) {
        List<ReviewVO> list = mapper.getListByUser(uCode);
        Map<String, ReviewVO> map = new HashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getRiCode(), k -> l));
        return map;
    }

    public boolean changeRvOwnCont(ReviewVO vo) {
        return mapper.insertReply(vo) == 1;
    }

    public boolean registerReview(ReviewVO vo) {
        return mapper.insertReview(vo) == 1;
    }

    public List<ReviewVO> getCurrentFive() {
        return mapper.getCurrentFive();
    }

    public boolean updateReview(ReviewVO vo) {
        return mapper.updateReview(vo) == 1;
    }
}
