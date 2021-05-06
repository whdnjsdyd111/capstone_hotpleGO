package com.example.demo.service.web;

import com.example.demo.domain.CommRecoVO;
import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.mapper.web.CommentMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CommentService {
    @Setter(onMethod_ = @Autowired)
    CommentMapper commentMapper;

    public List<CommentVO> commOdByReco(String bdCode){
        return commentMapper.commentOrderByReco(bdCode);
    }

    public List<CommentVO> commOdByRecent(String bdCode){
        return commentMapper.commentOrderByRecent(bdCode);
    }

    public List<CommentVO> commOdByWritenReply(String comCode){
        return commentMapper.commentOrderByWritenReply(comCode);
    }

    public boolean commentInsert(CommentVO commentVO){
        return commentMapper.commentInsert(commentVO) == 1;
    }

    public boolean commentUpdate(String uCode, String comCont){
        return commentMapper.commentUpdate(uCode, comCont) == 1;
    }

    public boolean commentDelete(String comCode){
        return commentMapper.commentDelete(comCode) == 1;
    }

    public boolean comRecoUp(String comCode) {
        return commentMapper.comRecommendUp(comCode) == 1;
    }

    public boolean comRecoDown(String comCode) {
        return commentMapper.comRecommendDown(comCode) == 1;
    }

    public boolean unComRecoUp(String comCode) {
        return commentMapper.unComRecommendUp(comCode) == 1;
    }

    public boolean unComRecoDown(String comCode) {
        return commentMapper.unComRecommendDown(comCode) == 1;
    }

    public boolean insertComReco(String comCode, String uCode, char reco) {
        return commentMapper.insertComReco(comCode, uCode, reco) == 1;
    }

    public boolean deleteComReco(String comCode, String uCode) {
        return commentMapper.deleteComReco(comCode, uCode) == 1;
    }

    public boolean updateComReco(String comCode, String uCode, char reco) {
        return commentMapper.updateComReco(comCode, uCode, reco) == 1;
    }

    public Map<String, String> getComReco(String bdCode, String uCode) {
        Map<String, String> map = new HashMap<>();
        commentMapper.getComReco(bdCode, uCode).forEach(l -> map.computeIfAbsent(l.getComCode(), k -> l.getReco()));
        return map;
    }

    public List<String> getComCodes(String uCode) {
        return commentMapper.commCodes(uCode);
    }

    public List<String> getReplyCodes(String uCode) {
        return commentMapper.replyCodes(uCode);
    }
}
