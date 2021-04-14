package com.example.demo.service;

import com.example.demo.domain.CourseInfoVO;
import com.example.demo.domain.CourseVO;
import com.example.demo.mapper.CourseMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CourseService {
    @Setter(onMethod_ = @Autowired)
    private CourseMapper mapper;

    public List<CourseVO> getMakingCourse(String uCode) {
        return mapper.getCourseN(uCode);
    }

    public Map<String, List<CourseInfoVO>> getMakingCourseInfo(String uCode) {
        List<CourseInfoVO> list = mapper.getCourseInfoN(uCode);
        Map<String, List<CourseInfoVO>> map = new LinkedHashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getCsCode(), k -> new ArrayList<>()).add(l));
        return map;
    }

    public List<CourseVO> getHistoryCourse(String uCode) {
        return mapper.getCourseY(uCode);
    }

    public Map<String, List<CourseInfoVO>> getHistoryCourseInfo(String uCode) {
        List<CourseInfoVO> list = mapper.getCourseInfoY(uCode);
        Map<String, List<CourseInfoVO>> map = new LinkedHashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getCsCode(), k -> new ArrayList<>()).add(l));
        return map;
    }

    public List<CourseVO> getAllCourse(String uCode) {
        return mapper.getCourseNC(uCode);
    }

    public Map<String, List<CourseInfoVO>> getAllInfo(String uCode) {
        List<CourseInfoVO> list = mapper.getCourseInfoNC(uCode);
        Map<String, List<CourseInfoVO>> map = new LinkedHashMap<>();
        list.forEach(l -> map.computeIfAbsent(l.getCsCode(), k -> new ArrayList<>()).add(l));
        return map;
    }

    public CourseVO getUsingCourse(String uCode) {
        return mapper.getCourseC(uCode);
    }

    public List<CourseInfoVO> getUsingCourseInfo(String uCode) {
        return mapper.getCourseInfoC(uCode);
    }

    public CourseVO getCourseDetail(String csCode) {
        return mapper.getCourse(csCode);
    }

    public List<CourseInfoVO> getCourseInfoDetail(String csCode) {
        return mapper.getCourseInfo(csCode);
    }

    public boolean register(CourseVO vo) {
        return mapper.insert(vo) == 1;
    }

    public void updateOrder(List<CourseInfoVO> vo, String csCode) {
        mapper.updateOrder(vo, csCode);
    }

    public boolean addCourse(String csCode, String htId) {
        return mapper.addCourse(csCode, htId) == 1;
    }

    public boolean alreadyAdded(String csCode, String htId) {
        return mapper.getCsHtId(csCode, htId) != null;
    }

    public void removeHtInCs(String csCode, String htId) {
        mapper.delHtInCs(csCode, htId);
    }

    public boolean removeCourse(String csCode) {
        return mapper.deleteCourse(csCode) == 1;
    }

    public boolean checkUsing(String uCode) {
        return mapper.selectUsing(uCode) != null;
    }

    public void changeUseCourse(String csCode) {
        mapper.updateUseCourse(csCode);
    }

    public void changeCourse(String uCode, String csCode) {
        mapper.updateChangeCourse(uCode, csCode);
    }

    public void returnCourse(String csCode) {
        mapper.updateReturnCourse(csCode);
    }
}
