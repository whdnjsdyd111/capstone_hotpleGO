package com.example.demo.mapper;

import com.example.demo.domain.CourseInfoVO;
import com.example.demo.domain.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    public List<CourseVO> getCourseN(String uCode);

    public List<CourseInfoVO> getCourseInfoN(String uCode);

    public List<CourseVO> getCourseY(String uCode);

    public List<CourseInfoVO> getCourseInfoY(String uCode);

    public List<CourseVO> getCourseNC(String uCode);

    public List<CourseInfoVO> getCourseInfoNC(String uCode);

    public CourseVO getCourseC(String uCode);

    public List<CourseInfoVO> getCourseInfoC(String uCode);

    public CourseVO getCourse(String csCode);

    public List<CourseInfoVO> getCourseInfo(String csCode);

    public int insert(CourseVO vo);

    public int addCourse(@Param("csCode") String csCode, @Param("htId") String htId);

    public String getCsHtId(@Param("csCode") String csCode, @Param("htId") String htId);

    public int delHtInCs(@Param("csCode") String csCode, @Param("htId") String htId);
}
