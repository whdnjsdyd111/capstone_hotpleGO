package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TasteMapper {
    public List<String> getTaste(String code);

    public int deleteAll(String code);

    public int insertAll(@Param("code") String code, @Param("ttCode") List<Integer> ttCode);
}
