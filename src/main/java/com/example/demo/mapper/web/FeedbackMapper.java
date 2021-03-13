package com.example.demo.mapper.web;

import com.example.demo.domain.web.FeedbackVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    public FeedbackVO read(String code);

    public List<FeedbackVO> getList(String s);

    public int insert(FeedbackVO vo);

    public int update(String code);

    public int delete(String code);
}
