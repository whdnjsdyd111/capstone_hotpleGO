package com.example.demo.service;

import com.example.demo.domain.OpenInfoVO;
import com.example.demo.mapper.OpenInfoMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class OpenInfoService {
    public static final int WEEKDAY = 1;
    public static final int SATURDAY = 2;
    public static final int SUNDAY = 3;


    @Setter(onMethod_ = @Autowired)
    OpenInfoMapper mapper;

    public boolean mergeOpen(String wo, String uCode, int kind) {
        if (kind == WEEKDAY)
            return mapper.updateWeekdayOpen(wo, uCode) == 1;
        else if (kind == SATURDAY)
            return mapper.updateSaturdayOpen(wo, uCode) == 1;
        else if (kind == SUNDAY)
            return mapper.updateSundayOpen(wo, uCode) == 1;
        else
            return false;
    }

    public boolean mergeBreak(String wb, String uCode, int kind) {
        if (kind == WEEKDAY)
            return mapper.updateWeekdayBreak(wb, uCode) == 1;
        else if (kind == SATURDAY)
            return mapper.updateSaturdayBreak(wb, uCode) == 1;
        else if (kind == SUNDAY)
            return mapper.updateSundayBreak(wb, uCode) == 1;
        else
            return false;
    }

    public List<OpenInfoVO> getList(long htId) {
        return mapper.select(htId);
    }

    public Map<String, String[]> getListByManager(String uCode) {
        Map<String, String[]> map = new HashMap<>();
        List<OpenInfoVO> list = mapper.selectByManager(uCode);
        list.forEach(l -> map.computeIfAbsent(l.getHtOb(), k -> l.getTCode().split("/")));
        return map;
    }
}
