package com.example.demo.mapper.web;

import com.example.demo.domain.CommRecoVO;
import com.example.demo.domain.web.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface CommentMapper {
    public List<CommentVO> commentOrderByReco(String bdCode);

    public List<CommentVO> commentOrderByRecent(String bdCode);

    public List<CommentVO> commentOrderByWritenReply(String bdCode);

    public int commentInsert(CommentVO commentVO);

    public int commentUpdate(String uCode, String comCont);

    public int commentDelete(String comCode);

    public int comRecommendUp(String comCode);

    public int comRecommendDown(String comCode);

    public int unComRecommendUp(String comCode);

    public int unComRecommendDown(String comCode);

    public int insertComReco(String comCode, String uCode, char reco);

    public int updateComReco(String comCode, String uCode, char reco);

    public int deleteComReco(String comCode, String uCode);

    public List<CommRecoVO> getComReco(String bdCode, String uCode);

}
