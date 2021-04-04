package com.example.demo.service;

import com.example.demo.domain.OpenInfoVO;
import com.example.demo.mapper.OpenInfoMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class OpenInfoService {
    public static final int WEEKDAY = 1;
    public static final int SATURDAY = 2;
    public static final int SUNDAY = 3;


    @Setter(onMethod_ = @Autowired)
    OpenInfoMapper mapper;

    public boolean mergeOpen(String wo, long htId, int kind) {
        if (kind == WEEKDAY)
            return mapper.updateWeekdayOpen(wo, htId) == 1;
        else if (kind == SATURDAY)
            return mapper.updateSaturdayOpen(wo, htId) == 1;
        else if (kind == SUNDAY)
            return mapper.updateSundayOpen(wo, htId) == 1;
        else
            return false;
    }

    public boolean mergeBreak(String wb, long htId, int kind) {
        if (kind == WEEKDAY)
            return mapper.updateWeekdayBreak(wb, htId) == 1;
        else if (kind == SATURDAY)
            return mapper.updateSaturdayBreak(wb, htId) == 1;
        else if (kind == SUNDAY)
            return mapper.updateSundayBreak(wb, htId) == 1;
        else
            return false;
    }

    public List<OpenInfoVO> getList(long htId) {
        return mapper.select(htId);
    }
}
