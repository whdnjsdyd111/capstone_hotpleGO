package com.example.demo.domain;

import lombok.Data;

@Data
public class MenuVO {
    private String meCode;
    private String meName;
    private long mePrice;
    private String meIntr;
    private String meCate;
    private String uuid;
    private String uploadPath;
    private String fileName;
}
