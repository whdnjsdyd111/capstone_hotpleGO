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
    private String Ucode;
    private String pw;
    private String nick;
    private Date birth;
    private Timestamp regdate;
    private Character gender;
    private String phone;
    private Long point;
}
