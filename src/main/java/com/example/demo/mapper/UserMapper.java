package com.example.demo.mapper;

import com.example.demo.domain.*;
import com.example.demo.domain.web.AdminVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<UserVO> userList();

    public UserVO read(String code);

    public UserVO readByEmail(String email);

    public int insert(UserVO vo);

    public AdminVO readAdmin(String code);

    public ManagerVO readManager(String code);

    public String readPassword(String code);

    public int insertManager(ManagerVO vo);

    public int updateNick(String nick, String uCode);

    public int updateAccount(ManagerVO vo);

    public int updatePw(@Param("pw") String pw, @Param("code") String code);

    public String getMbti(String uCode);

    public int updateMbti(@Param("mbti") String mbti, @Param("uCode") String uCode);

    public List<CourseVO> getPickCourseList(String uCode);

    public List<HotpleVO> getPickHotpleList(String uCode);

    public int pickHotple(PickListVO pickListVO);

    public int pickCourse(PickListVO pickListVO);

    public int deletePickHotple(String htId, String uCode);

    public int deletePickCourse(String csCode, String uCode);

    public HotpleVO getPickHotple(String uCode, String htId);
}
