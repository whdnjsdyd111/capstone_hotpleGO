package com.example.demo.security;

import com.example.demo.domain.UserVO;
import com.example.demo.mapper.UserMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    @Setter(onMethod_ = @Autowired)
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("Load User By UserName : " + userName);

        UserVO vo = userMapper.read(userName);

        log.warn("queried by member mapper: " + vo);

        return vo == null ? null : new CustomUser(vo);
    }
}
