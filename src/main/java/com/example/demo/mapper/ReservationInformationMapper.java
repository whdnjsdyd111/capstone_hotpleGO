package com.example.demo.mapper;

import com.example.demo.domain.ReservationInformationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationInformationMapper {
    public List<ReservationInformationVO> getCurList(String htId);

    public List<ReservationInformationVO> getHistoryList(String htId);
}
