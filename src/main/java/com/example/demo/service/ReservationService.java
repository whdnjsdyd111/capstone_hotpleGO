package com.example.demo.service;

import com.example.demo.domain.ReservationInformationVO;
import com.example.demo.domain.ReservationStatusVO;
import com.example.demo.mapper.ReservationMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    @Setter(onMethod_ = @Autowired)
    ReservationMapper mapper;

    public Map<ReservationInformationVO, List<ReservationStatusVO>> getList(Long htId) {
        return mapper.getReservation(htId);
    }
}
