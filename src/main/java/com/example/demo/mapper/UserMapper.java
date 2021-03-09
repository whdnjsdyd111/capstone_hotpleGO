package com.example.demo.mapper;

import com.example.demo.domain.ManagerVO;
import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.AdminVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserVO read(String code);

    public UserVO readByEmail(String email);

    public int insert(UserVO vo);

    public AdminVO readAdmin(String code);

    public int insertManager(ManagerVO vo);
}
