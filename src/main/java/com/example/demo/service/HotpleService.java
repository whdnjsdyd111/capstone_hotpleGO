package com.example.demo.service;

import com.example.demo.domain.HotpleVO;
import com.example.demo.mapper.HotpleMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotpleService {
    @Setter(onMethod_ = @Autowired)
    HotpleMapper mapper;

    public HotpleVO getIdByAddr(String addr) {
        return mapper.readAddr(addr);
    }

    public List<HotpleVO> getByUCode(String uCode) {
        return mapper.selectByManager(uCode);
    }

    public boolean registerBusn(HotpleVO vo) {
        return mapper.insertBusn(vo) == 1;
    }

    public boolean registerBusnGo(HotpleVO vo) {
        return mapper.insertBusnGo(vo) == 1;
    }
}
