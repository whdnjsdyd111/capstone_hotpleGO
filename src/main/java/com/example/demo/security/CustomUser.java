package com.example.demo.security;

import com.example.demo.domain.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class CustomUser extends User {
    private static final long serialVersionUID = 1L;

    private UserVO user;

    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CustomUser(UserVO vo) {
        super(vo.getUcode().split("/")[0], vo.getPw(), createRole(vo.getUcode()));
        log.info(vo);
        this.user = vo;
    }

    private static Collection<? extends GrantedAuthority> createRole(String code) {
        List<String> list = new ArrayList<>();
        list.add(code.split("/")[1]);

        return list.stream().map(auth -> new SimpleGrantedAuthority(auth)).collect(Collectors.toList());
    }
}
