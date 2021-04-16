package com.example.demo.domain.web;

import lombok.Data;

@Data
public class BoardVO {
    private String bdCode;
    private String bdTitle;
    private String bdArea;
    private String bdCont;
    private int bdRdCount;
    private int bdRecy;
    private int bdRecn;
    private int commCnt;
    private String uCode;

    /*참조컬럼*/
    private String nick;
}