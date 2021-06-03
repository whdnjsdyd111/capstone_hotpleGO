package com.example.demo;

import com.example.demo.domain.CourseVO;
import com.example.demo.domain.HotpleVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.Writer;
import java.util.*;

public class JavaTest {
    public static void main(String []args) throws Exception {
        Map<CourseVO, String> map = new HashMap<>();
        CourseVO vo = new CourseVO();
        vo.setCsCode("1111");
        vo.setCsTitle("zzz");
        map.put(vo, "a");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("map", new Gson().toJson(map));
        System.out.println(jsonObject.toString());
    }
}
