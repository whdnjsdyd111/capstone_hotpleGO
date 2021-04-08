package com.example.demo.mapper;

import com.example.demo.domain.ReservationAllVO;
import com.example.demo.domain.ReservationHotpleVO;
import com.example.demo.domain.ReservationInformationVO;
import com.example.demo.domain.SaleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    public List<ReservationInformationVO> getCurList(String htId);

    public List<ReservationInformationVO> getHistoryList(String htId);

    public List<ReservationAllVO> getReservationByManager(String uCode);

    public List<ReservationAllVO> getReservationByUser(String uCode);

    public List<ReservationHotpleVO> getReservationHotple(String uCode);

    public List<ReservationAllVO> getSales(String uCode);

    public List<ReservationInformationVO> getCurFive(String uCode);

    public List<ReservationAllVO> getCurListAll(String uCode);

    public int updateNoShow(String code, String str);

    public List<SaleVO> getSale(String uCode);
}
