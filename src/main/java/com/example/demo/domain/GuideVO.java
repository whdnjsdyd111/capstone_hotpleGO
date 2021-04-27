package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GuideVO {
    private String uCode;
    private Timestamp gRegDate;
    private Long gNoShow;
}
