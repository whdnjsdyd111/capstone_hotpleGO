package com.example.demo.service;

import com.example.demo.domain.GuideApplyVO;
import com.example.demo.domain.GuideVO;
import com.example.demo.mapper.GuideMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class GuideService {
    @Setter(onMethod_ = @Autowired)
    private GuideMapper guideMapper;

    public boolean insertGuide(GuideApplyVO guideApplyVO) {
        return guideMapper.insertGuide(guideApplyVO) == 1;
    }

    public GuideApplyVO checkGuide(String uCode) {
        return guideMapper.checkGuide(uCode);
    }

    public List<GuideApplyVO> getGuideList() {
        return guideMapper.getGuideApplyList();
    }

    public boolean confirmGuide(GuideVO guideVO) {
        return guideMapper.confirmGuide(guideVO) == 1;
    }

    public boolean removeGuideApply(String uCode) {
        return guideMapper.removeGuideApply(uCode) == 1;
    }

    public GuideVO yourGuide(String uCode){
        return guideMapper.yourGuide(uCode);
    }

    public List<GuideVO> guideList(){
        return guideMapper.getGuideList();
    }

    public boolean deleteGuide(String uCode) {
        return guideMapper.deleteGuide(uCode) == 1;
    }
}
