package com.example.demo.mapper;

import com.example.demo.domain.StatisticVO;
import com.example.demo.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    public String countUsers(String count);

    public String countBoard(String count);

    public String countComm(String count);

    public String countCourse(String count);

    public String countReport(String count);

    public String countAlc(String count);

    public int deleteContent(String bdCode);

    public List<StatisticVO> datesBoard();

    public List<StatisticVO> datesComm();

    public List<UserVO> todayUser();

    public String utilCsWith(String csWith);

    public List<StatisticVO> staBoardCount();

    public List<StatisticVO> staCommCount();
}
