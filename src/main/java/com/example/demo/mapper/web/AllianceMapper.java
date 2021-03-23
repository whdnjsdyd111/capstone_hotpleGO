package com.example.demo.mapper.web;

import com.example.demo.domain.web.AllianceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AllianceMapper {
    public AllianceVO read(String code);

    public List<AllianceVO> getList(String s);

    public int insert(AllianceVO vo);

    public int update(String code);

    public int delete(String code);
}
