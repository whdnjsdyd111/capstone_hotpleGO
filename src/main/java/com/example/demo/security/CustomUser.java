package com.example.demo.security;

import com.example.demo.domain.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Log4j2
public class CustomUser extends User {
    private static final long serialVersionUID = 1L;

    public UserVO user;

    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        log.info(authorities);
    }

    public CustomUser(UserVO vo) {
        super(vo.getUCode().split("/")[0], vo.getPw(),
                Collections.singleton(new SimpleGrantedAuthority(vo.getUCode().split("/")[1])));
        log.info(vo);
        this.user = vo;
    }
}
