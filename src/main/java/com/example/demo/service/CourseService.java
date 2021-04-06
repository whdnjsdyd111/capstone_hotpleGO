package com.example.demo.service;

import com.example.demo.domain.CourseInfoVO;
import com.example.demo.domain.CourseVO;
import com.example.demo.mapper.CourseMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
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

    public CourseVO getUsingCourse(String uCode) {
        return mapper.getCourseC(uCode);
    }

    public List<CourseInfoVO> getUsingCourseInfo(String uCode) {
        return mapper.getCourseInfoC(uCode);
    }
}
