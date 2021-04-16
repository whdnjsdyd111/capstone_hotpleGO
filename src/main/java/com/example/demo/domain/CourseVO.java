package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CourseVO {
    // 코스 정보
    private String csCode;
    private String csTitle;
    private String csWith;
    private Byte csNum;
    private Double csTemp;
    private String csWt;
    private Double csHum;
    private String uCode;
}
