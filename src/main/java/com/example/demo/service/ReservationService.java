package com.example.demo.service;

import com.example.demo.domain.ReservationAllVO;
import com.example.demo.domain.ReservationHotpleVO;
import com.example.demo.domain.ReservationInformationVO;
import com.example.demo.domain.SaleVO;
import com.example.demo.mapper.ReservationMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ReservationService {
    @Setter(onMethod_ = @Autowired)
    ReservationMapper mapper;

    public Map<String, List<ReservationAllVO>> getListByManager(String uCode) {
        List<ReservationAllVO> list = mapper.getReservationByManager(uCode);
        log.info(list);

        Map<String, List<ReservationAllVO>> map = new HashMap<>();
        list.forEach(l -> {
            map.computeIfAbsent(l.getRiCode(),
                    k -> new ArrayList<>()).add(l);
        });

        log.info(map);
        return map;
    }

    public Map<String, List<ReservationAllVO>> getList(String uCode) {
        List<ReservationAllVO> list = mapper.getReservationByUser(uCode);

        Map<String, List<ReservationAllVO>> map = new HashMap<>();
        list.forEach(l -> {
            map.computeIfAbsent(l.getRiCode(),
                    k -> new ArrayList<>()).add(l);
        });
        log.info(map);
        return map;
    }

    public List<ReservationHotpleVO> getHotples(String uCode) {
        return mapper.getReservationHotple(uCode);
    }

    public Map<String, List<ReservationAllVO>> getAllCurFive(String uCode) {
        List<ReservationAllVO> list = mapper.getCurListAll(uCode);
        Map<String, List<ReservationAllVO>> map = new HashMap<>();
        list.forEach(l -> {
            map.computeIfAbsent(l.getRiCode(),
                    k -> new ArrayList<>()).add(l);
            if (map.size() == 5) return;
        });

        log.info(map);
        return map;
    }

    public Map<String, List<ReservationAllVO>> getSales(String uCode) {
        List<ReservationAllVO> list = mapper.getSales(uCode);
        log.info(list);

        Map<String, List<ReservationAllVO>> map = new HashMap<>();
        list.forEach(l -> {
            String date = new SimpleDateFormat("yy-MM-dd").format(l.getRiTime());
            map.computeIfAbsent(date,
                    k -> new ArrayList<>()).add(l);
        });

        log.info(map);
        return map;
    }

    public List<ReservationInformationVO> getCurFive(String uCode) {
        return mapper.getCurFive(uCode);
    }

    public List<SaleVO> getSale(String uCode) {
        return mapper.getSale(uCode);
    }
}
