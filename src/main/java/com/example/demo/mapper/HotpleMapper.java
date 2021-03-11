package com.example.demo.mapper;

import com.example.demo.domain.HotpleVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotpleMapper {
    public Long readAddr(String str);

    public int insertBusn(HotpleVO vo);
}
