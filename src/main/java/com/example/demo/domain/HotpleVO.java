package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotpleVO {
    private Long htId;
    private Long busnNum;
    private String goId;
    private String htAddr;
    private String htAddrDet;
    private String htCont;
    private Long htTel;
    private String htImg;
    private Double htLat;
    private Double htLng;
    private String uCode;
    private int category;
    private String categoryName;
}
