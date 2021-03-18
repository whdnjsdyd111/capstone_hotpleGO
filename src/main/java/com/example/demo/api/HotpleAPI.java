package com.example.demo.api;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotpleAPI {
    public static String toDateStr(String code) {
        SimpleDateFormat fromFm = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat toFm = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        String dateStr = null;
        Date date = null;
        try {
            date = fromFm.parse(code.split("/")[0]);
            dateStr = toFm.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String timestampToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        String str = null;
        str = sdf.format(time);
        return str;
    }

    public static String codeToStr(String code) {
        code = code.replaceAll("/", "");
        return code;
    }

    public static String strToCode(String str) {
        StringBuilder builder = new StringBuilder(str);
        try {
            builder.insert(12, '/');
            if (str.length() >= 15) {
                builder.insert(15, '/');
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return builder.toString();
    }

    public static String replaceSlash(String url) {
        return url.replaceAll("\\\\", "/");
    }
}
