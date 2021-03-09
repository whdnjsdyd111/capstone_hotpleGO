package com.example.demo.domain.web;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class EventVO {
    private String eveId;
    private String eveTitle;
    private String eveCont;
    private Timestamp eveStart;
    private Timestamp eveExpi;
    private String uCode;
}