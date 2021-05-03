package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GuideApplyVO {
    private String uCode;
    private Timestamp gAplTime;
    private String gCont;
}
