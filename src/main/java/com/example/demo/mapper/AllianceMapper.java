package com.example.demo.mapper;

import com.example.demo.entity.AllianceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AllianceMapper {
    @Select("SELECT * FROM alliance")
    public List<AllianceVO> findAll();
}
