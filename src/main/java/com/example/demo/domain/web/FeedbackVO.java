package com.example.demo.domain.web;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FeedbackVO {
    private String feedCode;
    private String feedCont;
    private String uCode;

}
