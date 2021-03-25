package com.example.demo.service.web.implement;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;
import com.example.demo.service.web.BoardService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BoardImpl implements BoardService {
    @Setter(onMethod_ = @Autowired)
    private BoardMapper boardMapper;

    public boolean writeBoard(BoardVO boardVO) {
        int queryResult = 0;

        if (boardVO.getBdCode() == null) {
            queryResult = boardMapper.insertBoard(boardVO);
        } else {
            queryResult = boardMapper.updateBoard(boardVO);
        }

        return (queryResult == 1) ? true : false;
    }


    public boolean deleteBoard(String bdCode) {
        int queryResult = 0;

        BoardVO board = boardMapper.selectBoardDetail(bdCode);

        return (queryResult == 1) ? true : false;
    }

    @Override
    public List<BoardVO> getBoardList() {
        List<BoardVO> boardList = Collections.emptyList();

        return boardList;
    }
}
