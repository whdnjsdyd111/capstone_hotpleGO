package com.example.demo.service.web;

import com.example.demo.domain.Criteria;
import com.example.demo.domain.PageVO;
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

    public boolean recoUp(String bdCode) {
        return mapper.recommendUp(bdCode) == 1;
    }

    public boolean unRecoUp(String bdCode) {
        return mapper.unRecommendUp(bdCode) == 1;
    }

    public boolean recoDown(String bdCode) {
        return mapper.recommendDown(bdCode) == 1;
    }

    public boolean unRecoDownd(String bdCode) {
        return mapper.unRecommendDown(bdCode) == 1;
    }


    public boolean insertBoard(BoardVO boardVO){
        return mapper.insertBoard(boardVO) == 1;
    }

    public boolean deleteBoard(String bdCode, String uCode){
        return mapper.deleteBoard(bdCode, uCode) == 1;
    }

    public List<BoardVO> getBoardList(Criteria cri){
        return mapper.getBoardList(cri);
    }

    public List<BoardVO> getBoardsByKeyword(String keyword) {
        return mapper.getBoardsByKeyword(keyword);
    }

    public int getTotal(Criteria cri) {
        return mapper.getTotalBd(cri);
    }

    public BoardVO getBoardDetail(String bdCode){
        return mapper.selectBoardDetail(bdCode);
    }

    public boolean getCheck(String bdCode, String uCode) {
        return mapper.selectCheck(bdCode, uCode) == null;
    }


    public boolean updateBoard(BoardVO boardVO, String uCode){
        return mapper.updateBoard(boardVO, uCode) == 1;
    }

    public boolean upView(String bdCode) {
        return mapper.upView(bdCode) == 1;
    }

    public boolean insertReco(String bdCode, String uCode, char reco) {
        return mapper.insertReco(bdCode, uCode, reco) == 1;
    }

    public boolean deleteReco(String bdCode, String uCode) {
        return mapper.deleteReco(bdCode, uCode) == 1;
    }

    public boolean updateReco(String bdCode, String uCode, char reco) {
        return mapper.updateReco(bdCode, uCode, reco) == 1;
    }

    public String getReco(String bdCode, String uCode) {
        return mapper.getReco(bdCode, uCode);
    }

    public boolean getBookmark(String bdCode, String uCode) {
        return mapper.getBookmark(bdCode, uCode) != null;
    }

    public boolean removeBookmark(String bdCode, String uCode) {
        return mapper.deleteBookmark(bdCode, uCode) == 1;
    }

    public boolean insertBookmark(String bdCode, String uCode) {
        return mapper.insertBookmark(bdCode, uCode) == 1;
    }

    public List<BoardVO> getBookmarkList(BoardVO boardVO) {
        return mapper.getBookmarkList(boardVO);
    }

    public List<String> getBoardCodes(String uCode) {
        return mapper.getBoardCodes(uCode);
    }
}
