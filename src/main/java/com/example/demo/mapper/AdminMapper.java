package com.example.demo.mapper;

import com.example.demo.domain.ResStatisticsVO;
import com.example.demo.domain.StatisticVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
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

    public List<Integer> statistics();

    public List<ResStatisticsVO> res_statistics();

    public List<BoardVO> todayBoard();

    public List<CommentVO> todayComm();
}
