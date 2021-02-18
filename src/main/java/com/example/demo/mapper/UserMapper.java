package com.example.demo.mapper;

import com.example.demo.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserVO read(String email);

    public int insert(UserVO vo);

    public int update(UserVO vo);
}
