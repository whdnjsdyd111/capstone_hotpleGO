package com.example.demo.domain;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class UserVO {
    private String Ucode;
    private String pw;
    private String nick;
    private Date birth;
    private Timestamp regdate;
    private Character gender;
    private String phone;
    private long point;
}
