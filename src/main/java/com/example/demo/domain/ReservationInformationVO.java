package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ReservationInformationVO {
    private String riCode;
    private Timestamp riTime;
    private Short riPerson;
    private String riNoShow;
    private String riOdNum;
    private String riCont;
    private Long htId;
    private String uCode;
    private String nick;
    private String phone;
}
