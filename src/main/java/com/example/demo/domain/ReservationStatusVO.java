package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ReservationStatusVO {
    private String riCode;
    private Byte rsMeNum;
    private String uCode;
    private String meCode;
    private String meCate;
    private String meName;
    private long mePrice;
    private String meIntr;
    private String meHashTag;
    private String uuid;
    private String uploadPath;
    private String fileName;
}
