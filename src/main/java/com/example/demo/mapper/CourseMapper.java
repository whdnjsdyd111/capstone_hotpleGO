package com.example.demo.mapper;

import com.example.demo.domain.CourseInfoVO;
import com.example.demo.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    public List<CourseVO> getCourseN(String uCode);

    public List<CourseInfoVO> getCourseInfoN(String uCode);

    public CourseVO getCourseC(String uCode);

    public List<CourseInfoVO> getCourseInfoC(String uCode);
}
