package com.example.demo.service;

import com.example.demo.domain.UserVO;
import com.example.demo.domain.web.AdminVO;
import com.example.demo.mapper.UserMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Setter(onMethod_ = @Autowired)
    UserMapper userMapper;

    public UserVO get(String code) {
        return userMapper.read(code);
    }

    public UserVO getByEmail(String email) {
        return userMapper.readByEmail(email);
    }

    public boolean register(UserVO vo) {
        if (!vo.getPw().isEmpty()) {
            vo.setPw(new BCryptPasswordEncoder().encode(vo.getPw()));
        }
        return userMapper.insert(vo) == 1;
    }

    public AdminVO getAdmin(String code) {
        return userMapper.readAdmin(code);
    }
}
