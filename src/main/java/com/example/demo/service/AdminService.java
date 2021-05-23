package com.example.demo.service;

import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.web.BoardMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Setter(onMethod_ = @Autowired)
    private AdminMapper mapper;

    public String countUsers(String count){
        return mapper.countUsers(count);
    }

    public String countBoard(String count) {
        return mapper.countBoard(count);
    }

    public String countComm(String count) {
        return mapper.countComm(count);
    }

    public String countCourse(String count) {
        return mapper.countCourse(count);
    }

    public String countReport(String count) {
        return mapper.countReport(count);
    }

    public String countAlc(String count) {
        return mapper.countAlc(count);
    }

    public boolean deleteContent(String bdCode) {
        return mapper.deleteContent(bdCode) == 1;
    }
}
