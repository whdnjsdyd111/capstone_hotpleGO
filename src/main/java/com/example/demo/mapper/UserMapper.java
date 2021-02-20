package com.example.demo.mapper;

import com.example.demo.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserVO read(String code);

    public UserVO readByEmail(String email);

    public int insert(UserVO vo);

    public int insertOAuth2(UserVO vo);

}
