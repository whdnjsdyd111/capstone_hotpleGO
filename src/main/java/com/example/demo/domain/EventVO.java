package com.example.demo.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class EventVO {
    private String eveCode;
    private String eveTitle;
    private String eveCont;
    private Timestamp eveStart;
    private Timestamp eveExpi;
    private long eveRdCount;
    private String uCode;
    private String aName;
}