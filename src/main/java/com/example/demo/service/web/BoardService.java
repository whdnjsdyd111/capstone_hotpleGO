package com.example.demo.service.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;

import java.util.List;

public interface BoardService {

    public boolean registerBoard(BoardVO params);

    public BoardVO getBoardDetail(String bdCode);

    public boolean updateBoard(String bdCode);

    public boolean deleteBoard(String bdCode);

    public List<BoardVO> getBoardList();

}
