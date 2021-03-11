package com.example.demo.service;

import com.example.demo.mapper.HotpleMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotpleService {
    @Setter(onMethod_ = @Autowired)
    HotpleMapper mapper;

    public Long getIdByAddr(String addr) {
        return mapper.readAddr(addr);
    }
}
