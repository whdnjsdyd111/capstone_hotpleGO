package com.example.demo.service.implement;

import com.example.demo.domain.UserVO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {
    @Setter(onMethod_ = @Autowired)
    UserMapper userMapper;

    @Override
    public UserVO get(String code) {
        return userMapper.read(code);
    }

    @Override
    public UserVO getByEmail(String email) {
        return userMapper.readByEmail(email);
    }

    @Override
    public boolean register(UserVO vo) {
        if (!vo.getPw().isEmpty()) {
            vo.setPw(new BCryptPasswordEncoder().encode(vo.getPw()));
        }
        return userMapper.insert(vo) == 1;
    }
}
