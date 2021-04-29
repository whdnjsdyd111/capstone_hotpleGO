package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class UserVO {
    private String uCode;
    private String pw;
    private String nick;
    private Date birth;
    private Character gender;
    private String phone;
    private String profileImg;
    private Long point;
    private String mbti;
    private Timestamp regDate;
}
