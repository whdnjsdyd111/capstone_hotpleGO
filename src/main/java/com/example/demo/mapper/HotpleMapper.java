package com.example.demo.mapper;

import com.example.demo.domain.HotpleVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotpleMapper {
    public HotpleVO readAddr(String str);

    public int insertBusn(HotpleVO vo);

    public int insertBusnGo(HotpleVO vo);
}
