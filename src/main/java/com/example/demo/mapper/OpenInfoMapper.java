package com.example.demo.mapper;

import com.example.demo.domain.OpenInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpenInfoMapper {
    public int updateWeekdayOpen(@Param("wo") String wo, @Param("uCode") String uCode);

    public int updateWeekdayBreak(@Param("wb") String wb, @Param("uCode") String uCode);

    public int updateSaturdayOpen(@Param("wo") String wo, @Param("uCode") String uCode);

    public int updateSaturdayBreak(@Param("wb") String wb, @Param("uCode") String uCode);

    public int updateSundayOpen(@Param("wo") String wo, @Param("uCode") String uCode);

    public int updateSundayBreak(@Param("wb") String wb, @Param("uCode") String uCode);

    public List<OpenInfoVO> select(long htId);

    public List<OpenInfoVO> selectByManager(String uCode);

    public int insertHoliday(@Param("week") String week, @Param("day") String day, @Param("uCode") String uCode);

    public int deleteHoliday(@Param("code") String code, @Param("uCode") String uCode);

    public List<String> getHoliday(String uCode);
}
