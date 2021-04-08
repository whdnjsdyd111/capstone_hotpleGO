package com.example.demo.mapper;

import com.example.demo.domain.ReservationAllVO;
import com.example.demo.domain.ReservationHotpleVO;
import com.example.demo.domain.ReservationInformationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    public List<ReservationInformationVO> getCurList(String htId);

    public List<ReservationInformationVO> getHistoryList(String htId);

    public List<ReservationAllVO> getReservation(Long htId);

    public List<ReservationAllVO> getReservationByUser(String uCode);

    public List<ReservationHotpleVO> getReservationHotple(String uCode);

    public List<ReservationAllVO> getSales(Long htId);

    public List<ReservationInformationVO> getCurFive(Long htId);

    public List<ReservationAllVO> getCurListAll(Long htId);

    public int updateNoShow(String code, String str);
}
