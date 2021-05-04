package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PickListVO {
    private String uCode;
    private String csCode;
    private String htId;
    private Timestamp pickTime;
}
