package com.example.demo.service.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;

import java.util.List;

public interface BoardService {

    public boolean writeBoard(BoardVO boardVO);

    public boolean deleteBoard(String bdCode);

    public List<BoardVO> getBoardList();

}
