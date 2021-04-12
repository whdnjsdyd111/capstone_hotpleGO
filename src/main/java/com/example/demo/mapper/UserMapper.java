package com.example.demo.mapper;

import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.AdminVO;
import com.example.demo.domain.ManagerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    public UserVO read(String code);

    public UserVO readByEmail(String email);

    public String getMbti(String uCode);

    public int insert(UserVO vo);

    public AdminVO readAdmin(String code);

    public ManagerVO readManager(String code);

    public String readPassword(String code);

    public int insertManager(ManagerVO vo);

    public int updateNick(@Param("nick") String nick, @Param("code") String code);

    public int updateAccount(ManagerVO vo);

    public int updatePw(@Param("pw") String pw, @Param("code") String code);

    public int updateMbti(@Param("mbti") String mbti, @Param("uCode") String uCode);
}
