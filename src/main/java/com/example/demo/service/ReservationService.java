package com.example.demo.service;

import com.example.demo.domain.ReservationAllVO;
import com.example.demo.domain.ReservationInformationVO;
import com.example.demo.domain.ReservationStatusVO;
import com.example.demo.mapper.ReservationMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ReservationService {
    @Setter(onMethod_ = @Autowired)
    ReservationMapper mapper;

    public Map<String, List<ReservationAllVO>> getList(Long htId) {
        List<ReservationAllVO> list = mapper.getReservation(htId);
        log.info(list);

        Map<String, List<ReservationAllVO>> map = new HashMap<>();
        list.forEach(l -> {
            map.computeIfAbsent(l.getRiCode(),
                    k -> new ArrayList<>()).add(l);
        });

        log.info(map);
        return map;
    }
}