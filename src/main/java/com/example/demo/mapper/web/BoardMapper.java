package com.example.demo.mapper.web;

import com.example.demo.domain.web.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    public List<BoardVO> getBoardList(BoardVO boardVO);

    public BoardVO selectBoardDetail(String bdCode);

    public int insertBoard(BoardVO boardVO);

    public int updateBoard(BoardVO bdCode);

    public int deleteBoard(String bdCode);

}
