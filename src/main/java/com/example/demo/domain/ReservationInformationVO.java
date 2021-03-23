package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

@Data
public class ReservationInformationVO {
    private String riCode;
    private Long htId;
    private Timestamp riTime;
    private Short riPerson;
    private String riNoShow;
    private String riOdNum;
    private String riCont;
    private String uCode;

    private Map<MenuVO, Integer> rs;
}
