package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotpleVO {
    private Long htId;
    private String busnNum;
    private String goId;
    private String busnName;
    private String htAddr;
    private String htAddrDet;
    private Long htZip;
    private String htCont;
    private Double goGrd;
    private String htTel;
    private String htImg;
    private String uploadPath;
    private String fileName;
    private Double htLat;
    private Double htLng;
    private String uCode;
    private Long category;
    private String ttKind;
}
