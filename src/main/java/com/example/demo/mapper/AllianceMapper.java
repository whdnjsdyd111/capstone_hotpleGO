package com.example.demo.mapper;

import com.example.demo.domain.AllianceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AllianceMapper {
    public List<AllianceVO> findAll();
}
