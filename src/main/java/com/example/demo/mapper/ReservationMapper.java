package com.example.demo.mapper;

import com.example.demo.domain.*;
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

    public ReservationInfoVO getByCode(String riCode);

    public int updateNoShow(String code, String str);

    public List<SaleVO> getSale(String uCode);

    public int insertRes(ReservationInfoVO vo);

    public int insertResStatus(List<ReservationStatusVO> vo);

    public long getTotalFeeByRiCode(String riCode);

    public int deleteRes(String riCode);
}
