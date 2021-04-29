package com.example.demo;

import com.example.demo.domain.HotpleVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.Writer;
import java.util.*;

public class JavaTest {
    public static void main(String []args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("a", "b");
        System.out.print(map);
        System.out.print(map.containsKey("a"));
    }
}
