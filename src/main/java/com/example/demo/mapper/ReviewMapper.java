package com.example.demo.mapper;

import com.example.demo.domain.ReviewVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    public List<ReviewVO> getList(long htId);

    public List<ReviewVO> getListByManager(String uCode);

    public List<ReviewVO> getListByUser(String uCode);

    public List<ReviewVO> getCurrentFive();

    public List<Integer> getRatings(String uCode);

    public List<Integer> getRatingsHotple(Long htId);

    public int insertReply(ReviewVO vo);

    public int insertReview(ReviewVO vo);

    public int updateReview(ReviewVO vo);

}
