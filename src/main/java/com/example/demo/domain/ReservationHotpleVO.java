package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
public class ReservationHotpleVO {

    // 예약 정보
    private String riCode;
    private Timestamp riTime;
    private String riPerson;
    private String riNoShow;
    private String riCont;

    // 핫플 정보
    private long htId;
    private String busnName;
    private String htAddr;
    private String htAddrDet;
    private String htCont;
    private String htTel;

    // 핫플 사진
    private String uuid;
    private String uploadPath;
    private String fileName;
}
