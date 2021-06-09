package com.example.demo.service;

import com.example.demo.domain.StatisticVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.web.BoardMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Setter(onMethod_ = @Autowired)
    private AdminMapper mapper;

    public static final int COMM = 0;
    public static final int BOARD = 1;

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

    public List<StatisticVO> getContentStatistic(int kind) {
        if (kind == COMM)
            return mapper.datesComm();
        else
            return mapper.datesBoard();
    }

    public List<StatisticVO> staCount(int kind){
        if (kind == COMM)
            return mapper.staCommCount();
        else
            return mapper.staBoardCount();
    }

    public List<UserVO> getTodayUser() {
        return mapper.todayUser();
    }

    public List<BoardVO> getTodayBoard(){
        return mapper.todayBoard();
    }

    public List<CommentVO> getTodayComm(){
        return mapper.todayComm();
    }

    public String utilAlone() {
        return mapper.utilCsWith("혼자");
    }
    public String utilCouple() {
        return mapper.utilCsWith("커플");
    }
    public String utilFamily() {
        return mapper.utilCsWith("가족");
    }
    public String utilFriend() {
        return mapper.utilCsWith("친구");
    }

//    public String staBoardCount(){
//        return mapper.staBoardCount();
//    }

}
