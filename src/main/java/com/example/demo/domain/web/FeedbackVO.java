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

    public String toDateStr(String feedCode) {
        SimpleDateFormat fromFm = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat toFm = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        String dateStr = null;
        Date date = null;
        try {
            date = fromFm.parse(feedCode.split("/")[0]);
            dateStr = toFm.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
