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

    public ReservationStatusVO(ReservationAllVO vo) {
        riCode = vo.getRiCode();
        rsMeNum = vo.getRsMeNum();
        uCode = vo.getUCode();
        meCode = vo.getMeCode();
        meCate = vo.getMeCate();
        meName = vo.getMeName();
        mePrice = vo.getMePrice();
        meIntr = vo.getMeIntr();
        meHashTag = vo.getMeHashTag();
        uuid = vo.getUuid();
        uploadPath = vo.getUploadPath();
        fileName = vo.getFileName();
    }
}
