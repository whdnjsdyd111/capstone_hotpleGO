package com.example.demo.mapper.web;

import com.example.demo.domain.web.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    public int insertBoard(BoardVO params);

    public BoardVO selectBoardDetail(String bdCode);

    public int updateBoard(BoardVO params);

    public int deleteBoard(String bdCode);

    public List<BoardVO> selectBoardList();

    public int selectBoardTotalCount();


}
