package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ManagerVO {
    private String uCode;
    private String pw;
    private String nick;
    private Date birth;
    private Character gender;
    private String phone;
    private String profileImg;
    private Long point;
    private Timestamp regDate;
    private String mName;
    private String mAccount;
}
