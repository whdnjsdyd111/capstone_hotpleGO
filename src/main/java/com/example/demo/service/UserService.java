package com.example.demo.service;

import com.example.demo.domain.UserVO;

public interface UserService {

    /**
     *
     * @param code
     * @return 코드로 조회한 회원
     */
    public UserVO get(String code);

    /**
     *
     * @param email
     * @return 이메일로 조회한 회원
     */
    public UserVO getByEmail(String email);

    /**
     *
     * @param vo
     * @return 회원 등록 여부
     */
    public boolean register(UserVO vo);
}
