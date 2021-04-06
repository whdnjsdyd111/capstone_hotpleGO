package com.example.demo.api;

import com.example.demo.domain.ReservationAllVO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HotpleAPI {

    public static final int IMAGE = 0;
    public static final int THUMBNAIL = 1;

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

    public static int sumMenusPrice(List<ReservationAllVO> list) {
        int sum = 0;
        for (ReservationAllVO reservationAllVO : list) {
            sum += reservationAllVO.getMePrice() * reservationAllVO.getRsMeNum();
        }
        return sum;
    }

    public static int reservHistoryNum(Map<String, List<ReservationAllVO>> map) {
        int num = 0;
        Timestamp time = new Timestamp(System.currentTimeMillis());

        for (String key : map.keySet()) {
            List<ReservationAllVO> list = map.get(key);
            if (time.after(list.get(0).getRiTime())) num++;
        }

        return num;
    }

    public static int reservCurNum(Map<String, List<ReservationAllVO>> map) {
        int num = 0;
        Timestamp time = new Timestamp(System.currentTimeMillis());

        for (String key : map.keySet()) {
            List<ReservationAllVO> list = map.get(key);
            if (time.before(list.get(0).getRiTime())) num++;
        }

        return num;
    }

    public static boolean reservBol(Timestamp time) {
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        return curTime.before(time);
    }

    public static String toTel(String str) {
        if (str == null) return null;
        StringBuffer sb = new StringBuffer(str);
        sb.insert(7, '-');
        sb.insert(3, '-');
        return sb.toString();
    }

    public static String tCodeToTime(String tCode) {
        String[] str = tCode.split("/");
        for (int i = 0; i < 2; i++) {
            str[i] = new StringBuilder(str[i]).insert(2, ":").toString();
        }
        return str[0] + " ~ " + str[1];
    }

    public static String contSubstring(String cont) {
        if (cont.length() > 8) {
            return cont.substring(0, 8) + "..";
        } else {
            return cont;
        }
    }

    public static Long sumMenuPrice(List<ReservationAllVO> list) {
        long sum = 0;
        for (ReservationAllVO vo : list) {
            sum += vo.getRsMeNum() * vo.getMePrice();
        }
        return sum;
    }

    public static String toImg(String uploadPath, String uuid, String fileName, int kind) {
        if (uploadPath == null || uuid == null || fileName == null) {
            return "/images/logo.jpg";
        }

        String imgSrc = "/hotpleImage/";
        imgSrc += replaceSlash(uploadPath) + "/";

        if (kind == IMAGE) imgSrc += uuid + "_" + fileName;
        if (kind == THUMBNAIL) imgSrc += "s_" + uuid + "_" + fileName;

        return imgSrc;
    }
}
