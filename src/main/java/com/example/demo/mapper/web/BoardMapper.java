package com.example.demo.mapper.web;

import com.example.demo.domain.Criteria;
import com.example.demo.domain.web.BoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    public List<BoardVO> getBoardList(Criteria cri);

    public int getTotalBd(Criteria cri);

    public List<BoardVO> getBoardsByKeyword(String keyword);

    public BoardVO selectBoardDetail(String bdCode);

    public String selectCheck(String bdCode, String uCode);

    public int recommendUp(String bdCode);

    public int unRecommendUp(String bdCode);

    public int recommendDown(String bdCode);

    public int unRecommendDown(String bdCode);

    public int insertBoard(BoardVO boardVO);

    public int updateBoard(BoardVO vo, String uCode);

    public int deleteBoard(String bdCode, String uCode);

    public int upView(String bdCode);

    public int insertReco(String bdCode, String uCode, char reco);

    public int deleteReco(String bdCode, String uCode);

    public int updateReco(String bdCode, String uCode, char reco);

    public String getReco(String bdCode, String uCode);

    public String getBookmark(String bdCode, String uCode);

    public int deleteBookmark(String bdCode, String uCode);

    public int insertBookmark(String bdCode, String uCode);

    public List<BoardVO> getBookmarkList(BoardVO boardVO);

    public List<BoardVO> getBoardCodes(String uCode);

}
