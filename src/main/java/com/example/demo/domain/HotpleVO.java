package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotpleVO {
    private Long htId;
    private Long busnNum;
    private String goId;
    private String busnName;
    private String htAddr;
    private String htAddrDet;
    private int htZip;
    private String htCont;
    private Double goGrd;
    private Long htTel;
    private String htImg;
    private Double htLat;
    private Double htLng;
    private String uCode;
    private int category;
    private String categoryName;
}
