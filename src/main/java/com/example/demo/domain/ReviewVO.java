package com.example.demo.domain;

import lombok.Data;

@Data
public class ReviewVO {
    private String riCode;
    private Integer rvRating;
    private String rvCont;
    private String rvOwnCont;
    private String rvImg;
    private String uCode;
}
