package com.example.demo.domain.web;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Data
@Log4j2
public class AllianceVO {
    private String alcCode;
    private String name;
    private String email;
    private String phone;
    private String content;
}
