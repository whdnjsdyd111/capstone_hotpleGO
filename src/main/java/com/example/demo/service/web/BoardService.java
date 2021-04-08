package com.example.demo.service.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Log4j2
public class BoardService {
    @Setter(onMethod_ = @Autowired)
    private BoardMapper mapper;
    public boolean updateBoard(BoardVO boardVO){
        return mapper.updateBoard(boardVO) == 1;
    }

    public boolean upLike(String bdRecy) {
        return mapper.recommendUp(bdRecy) == 1;
    }

    public boolean downLike(String bdRecn) {
        return mapper.recommendDown(bdRecn) == 1;
    }

    public boolean insertBoard(BoardVO boardVO){
        return mapper.insertBoard(boardVO) == 1;
    }

    public boolean deleteBoard(String bdCode){
        return mapper.deleteBoard(bdCode) == 1;
    }

    public List<BoardVO> getBoardList(BoardVO boardVO){
        return mapper.getBoardList(boardVO);
    }

    public BoardVO getBoardDetail(String bdCode){
        return mapper.selectBoardDetail(bdCode);
    }

    public boolean upView(String bdCode) {
        return mapper.upView(bdCode) == 1;
    }
}
