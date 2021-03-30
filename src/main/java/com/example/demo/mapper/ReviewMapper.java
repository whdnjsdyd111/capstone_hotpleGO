package com.example.demo.mapper;

import com.example.demo.domain.ReviewVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    public List<ReviewVO> getList(long htId);
}
