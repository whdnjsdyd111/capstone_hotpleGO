package com.example.demo.service;

import com.example.demo.mapper.TasteMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasteService {
    @Setter(onMethod_ = @Autowired)
    TasteMapper mapper;

    public List<String> getTasteList(String code) {
        return mapper.getTaste(code);
    }

    public void reset(String code) {
        mapper.deleteAll(code);
    }

    public boolean registerAll(String code, List<Integer> ttCode) {
        return mapper.insertAll(code, ttCode) > 0;
    }
}
