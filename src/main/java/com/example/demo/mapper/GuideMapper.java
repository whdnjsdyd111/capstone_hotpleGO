package com.example.demo.mapper;

import com.example.demo.domain.GuideApplyVO;
import com.example.demo.domain.GuideVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GuideMapper {
    public int insertGuide(GuideApplyVO guideApplyVO);

    public GuideApplyVO checkGuide(String uCode);

    public List<GuideApplyVO> getGuideApplyList();

    public int confirmGuide(GuideVO guideVO);

    public int removeGuideApply(String uCode);

    public GuideVO yourGuide(String uCode);
}
