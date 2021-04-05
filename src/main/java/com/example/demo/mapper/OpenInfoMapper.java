package com.example.demo.mapper;

import com.example.demo.domain.OpenInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpenInfoMapper {
    public int updateWeekdayOpen(@Param("wo") String wo, @Param("htId") long htId);

    public int updateWeekdayBreak(@Param("wb") String wb, @Param("htId") long htId);

    public int updateSaturdayOpen(@Param("wo") String wo, @Param("htId") long htId);

    public int updateSaturdayBreak(@Param("wb") String wb, @Param("htId") long htId);

    public int updateSundayOpen(@Param("wo") String wo, @Param("htId") long htId);

    public int updateSundayBreak(@Param("wb") String wb, @Param("htId") long htId);

    public List<OpenInfoVO> select(long htId);
}
