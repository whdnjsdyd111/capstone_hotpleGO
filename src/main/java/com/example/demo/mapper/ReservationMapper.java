package com.example.demo.mapper;

import com.example.demo.domain.ReservationAllVO;
import com.example.demo.domain.ReservationInformationVO;
import com.example.demo.domain.ReservationStatusVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {
    public List<ReservationInformationVO> getCurList(String htId);

    public List<ReservationInformationVO> getHistoryList(String htId);

    public List<ReservationAllVO> getReservation(Long htId);
}
