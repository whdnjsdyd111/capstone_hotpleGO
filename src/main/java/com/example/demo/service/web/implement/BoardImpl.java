package com.example.demo.service.web.implement;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;
import com.example.demo.service.web.BoardService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardImpl implements BoardService {
    @Setter(onMethod_ = @Autowired)
    private BoardMapper boardMapper;

    public boolean registerBoard(BoardVO params){
        int queryResult = 0;

        if (params.getBdCode() == null) {
            queryResult = boardMapper.insertBoard(params);
        } else {
            queryResult = boardMapper.updateBoard(params);
        }

        return (queryResult == 1) ? true : false;
    }

    public BoardVO getBoardDetail(String bdCode){
        return boardMapper.selectBoardDetail(bdCode);
    }

    @Override
    public boolean updateBoard(String bdCode) {
        return false;
    }

    public boolean deleteBoard(String bdCode){
        return boardMapper.deleteBoard(bdCode) == 1;
    }

    public List<BoardVO> getBoardList(){
        return boardMapper.selectBoardList();
    }
}
