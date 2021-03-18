package com.example.demo.mapper;

import com.example.demo.domain.HotpleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotpleMapper {
    public HotpleVO readAddr(String str);

    public List<HotpleVO> selectByManager(String uCode);

    public int insertBusn(HotpleVO vo);

    public int insertBusnGo(HotpleVO vo);
}
