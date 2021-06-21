package com.example.demo;

import com.example.demo.domain.CourseVO;
import com.example.demo.domain.HotpleVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.Writer;
import java.util.*;

public class JavaTest {
    public static void main(String []args) throws Exception {
        String str = "[\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시메이진 대구백화점\",\n" +
                "        \"GOGRD\": 3.7,\n" +
                "        \"GOID\": \"ChIJEUrVfMTjZTURT6KmgDU4xbA\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 동성로2가 174\",\n" +
                "        \"HTLAT\": 35.869303,\n" +
                "        \"HTLNG\": 128.5959402,\n" +
                "        \"HTTEL\": \"0532552655\",\n" +
                "        \"T/O\": \"1130/2200\",\n" +
                "        \"W/O\": \"1130/2200\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시아리\",\n" +
                "        \"GOGRD\": 3,\n" +
                "        \"GOID\": \"ChIJy4G9iI3hZTURJP3LOqGrrh4\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"동구 신천동 동부로 149\",\n" +
                "        \"HTLAT\": 35.877739,\n" +
                "        \"HTLNG\": 128.6286791,\n" +
                "        \"HTTEL\": \"0536611730\",\n" +
                "        \"T/O\": \"1030/2100\",\n" +
                "        \"W/O\": \"1030/2000\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"뉴욕초밥\",\n" +
                "        \"GOGRD\": 4.3,\n" +
                "        \"GOID\": \"ChIJhZ9r30XjZTURSHtoJ8p84iY\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 동성로2가 41-1\",\n" +
                "        \"HTLAT\": 35.8709805,\n" +
                "        \"HTLNG\": 128.5960813,\n" +
                "        \"HTTEL\": \"0532560902\",\n" +
                "        \"T/O\": \"1030/2100\",\n" +
                "        \"W/O\": \"1030/2000\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"종로초밥\",\n" +
                "        \"GOGRD\": 4,\n" +
                "        \"GOID\": \"ChIJ3bkfKsLjZTURXfPrruI1Who\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 종로1가 국채보상로 558\",\n" +
                "        \"HTLAT\": 35.8704926,\n" +
                "        \"HTLNG\": 128.5916669,\n" +
                "        \"HTTEL\": \"0532520321\",\n" +
                "        \"T/O\": \"1100/2200\",\n" +
                "        \"W/O\": \"1100/2200\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시다이닝준\",\n" +
                "        \"GOGRD\": 2.3,\n" +
                "        \"GOID\": \"ChIJIwPCw3DjZTURmYfGPata_zQ\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"북구 태평로 161 지하 2층\",\n" +
                "        \"HTLAT\": 35.8758942,\n" +
                "        \"HTLNG\": 128.5963119,\n" +
                "        \"HTTEL\": \"0532520321\",\n" +
                "        \"T/O\": \"1100/2200\",\n" +
                "        \"W/O\": \"1100/2200\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"여부초밥\",\n" +
                "        \"GOGRD\": 0,\n" +
                "        \"GOID\": \"ChIJO_VGebvhZTURIb-V5ILzmoE\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"북구 복현동 360-5번지 1층\",\n" +
                "        \"HTLAT\": 35.8940581,\n" +
                "        \"HTLNG\": 128.62388,\n" +
                "        \"HTTEL\": \"0539393663\",\n" +
                "        \"T/O\": \"1100/2200\",\n" +
                "        \"W/O\": \"1100/2200\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시201 신천점\",\n" +
                "        \"GOGRD\": 3.9,\n" +
                "        \"GOID\": \"ChIJ839L-gjhZTUR0kJ02yiZj2o\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"동구 신천1.2동 송라로 63\",\n" +
                "        \"HTLAT\": 35.8728602,\n" +
                "        \"HTLNG\": 128.6182864,\n" +
                "        \"HTTEL\": \"0539393663\",\n" +
                "        \"T/O\": \"1100/2200\",\n" +
                "        \"W/O\": \"1100/2200\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"마사\",\n" +
                "        \"GOGRD\": 4.4,\n" +
                "        \"GOID\": \"ChIJt2LXsH7hZTURIMn5xaL2xBM\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"북구 산격동 산격로6길 18\",\n" +
                "        \"HTLAT\": 35.8924524,\n" +
                "        \"HTLNG\": 128.6071754,\n" +
                "        \"HTTEL\": \"0532476678\",\n" +
                "        \"T/O\": \"1100/2100\",\n" +
                "        \"W/O\": \"1100/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"호야초밥\",\n" +
                "        \"GOGRD\": 0,\n" +
                "        \"GOID\": \"ChIJ8bWkLynjZTUR2z3_vkNIf6w\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"북구 칠성동 칠성로 25-6\",\n" +
                "        \"HTLAT\": 35.8802989,\n" +
                "        \"HTLNG\": 128.592503,\n" +
                "        \"HTTEL\": \"01053347908\",\n" +
                "        \"T/O\": \"1100/2100\",\n" +
                "        \"W/O\": \"1100/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시하루나\",\n" +
                "        \"GOGRD\": 4.5,\n" +
                "        \"GOID\": \"ChIJWT_KZIfhZTUROLPteyiegtA\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"동구 신천동 733-1\",\n" +
                "        \"HTLAT\": 35.8729957,\n" +
                "        \"HTLNG\": 128.6182244,\n" +
                "        \"HTTEL\": \"01053347908\",\n" +
                "        \"T/O\": \"1100/2100\",\n" +
                "        \"W/O\": \"1100/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"코이요\",\n" +
                "        \"GOGRD\": 3.5,\n" +
                "        \"GOID\": \"ChIJRR6NHrbjZTURzusIP0CLSgg\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 문화동 국채보상로123길 7\",\n" +
                "        \"HTLAT\": 35.8711591,\n" +
                "        \"HTLNG\": 128.5972807,\n" +
                "        \"HTTEL\": \"01053347908\",\n" +
                "        \"T/O\": \"1100/2100\",\n" +
                "        \"W/O\": \"1100/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시센\",\n" +
                "        \"GOGRD\": 2,\n" +
                "        \"GOID\": \"ChIJmawnYmfhZTURGQVuQ2RQC3Q\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"북구 침산동 번지 103동 동1층 269-10 B9호\",\n" +
                "        \"HTLAT\": 35.8893813,\n" +
                "        \"HTLNG\": 128.5901516,\n" +
                "        \"HTTEL\": \"0533557212\",\n" +
                "        \"T/O\": \"1100/2100\",\n" +
                "        \"W/O\": \"1100/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"화담\",\n" +
                "        \"GOGRD\": 4.2,\n" +
                "        \"GOID\": \"ChIJ-xBbAMXjZTUR4o1ehfoQSsQ\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 공평동 동성로3길 75\",\n" +
                "        \"HTLAT\": 35.8687544,\n" +
                "        \"HTLNG\": 128.598039,\n" +
                "        \"HTTEL\": \"0532540920\",\n" +
                "        \"T/O\": \"1200/2100\",\n" +
                "        \"W/O\": \"1200/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"행복한초밥\",\n" +
                "        \"GOGRD\": 4,\n" +
                "        \"GOID\": \"ChIJKd-jAoLhZTURCrwCpI_9Wzs\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"동구 팔공로 51 길 11 114호\",\n" +
                "        \"HTLAT\": 35.9230702,\n" +
                "        \"HTLNG\": 128.6400202,\n" +
                "        \"HTTEL\": \"0539823606\",\n" +
                "        \"T/O\": \"1100/2100\",\n" +
                "        \"W/O\": \"1100/2100\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"바르미스시뷔페 동성로점\",\n" +
                "        \"GOGRD\": 4.2,\n" +
                "        \"GOID\": \"ChIJV7ol3c3jZTURi7kqFBut2Fo\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 성내1동 동성로3길 84\",\n" +
                "        \"HTLAT\": 35.8686787,\n" +
                "        \"HTLNG\": 128.5987615,\n" +
                "        \"HTTEL\": \"0532302800\",\n" +
                "        \"T/O\": \"1200/2200\",\n" +
                "        \"W/O\": \"1200/2200\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"스시이로까\",\n" +
                "        \"GOGRD\": 4.1,\n" +
                "        \"GOID\": \"ChIJdZt458PjZTURKndYpRTMvuA\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 남일동 중앙대로 394\",\n" +
                "        \"HTLAT\": 35.8682972,\n" +
                "        \"HTLNG\": 128.594001,\n" +
                "        \"HTTEL\": \"0532568259\",\n" +
                "        \"T/O\": \"1130/2130\",\n" +
                "        \"W/O\": \"1130/2130\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"초밥회일번지\",\n" +
                "        \"GOGRD\": 0,\n" +
                "        \"GOID\": \"ChIJbXxZBI3hZTURNXj0SPs4abU\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"동구 신천동 442-1\",\n" +
                "        \"HTLAT\": 35.8755748,\n" +
                "        \"HTLNG\": 128.6298585,\n" +
                "        \"HTTEL\": \"0537442888\",\n" +
                "        \"T/O\": \"1130/2130\",\n" +
                "        \"W/O\": \"1130/2130\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"바르미스시 동대구터미널점\",\n" +
                "        \"GOGRD\": 1.3,\n" +
                "        \"GOID\": \"ChIJbXCClJLhZTURARcub84BdIU\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"동구 신천동 동부로 149\",\n" +
                "        \"HTLAT\": 35.8778125,\n" +
                "        \"HTLNG\": 128.6293125,\n" +
                "        \"HTTEL\": \"0537533280\",\n" +
                "        \"T/O\": \"1130/2130\",\n" +
                "        \"W/O\": \"1130/2130\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"BUSNNAME\": \"긴류오사카\",\n" +
                "        \"GOGRD\": 4.1,\n" +
                "        \"GOID\": \"ChIJTRq6yurjZTURiSpIpBJc_oU\",\n" +
                "        \"GOING\": \"https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=ATtYBwLhqvpbPyOl1J0xAzzhF-rt0YdwDm6sIqsWHApzL0usbIjVTLz_IzJ4CAZfBV6UYR3bzH5SGQgVHInM9Kjt-te2dInajV7BkivdrVMsjB9YaJiYlZB2lsY9UyqI1UuOPJ0oNmMtdEaB2tNUjhXh7u5yM99NI2g9GsyoOazuv-9oCN7Y&key=AIzaSyA45GpbkYWZji6FyYrrKdG3Ld-8NoMH6SY\",\n" +
                "        \"HTADDR\": \"중구 종로1가 13-2\",\n" +
                "        \"HTLAT\": 35.8708963,\n" +
                "        \"HTLNG\": 128.5912303,\n" +
                "        \"HTTEL\": \"0537533280\",\n" +
                "        \"T/O\": \"1130/2130\",\n" +
                "        \"W/O\": \"1130/2130\"\n" +
                "    }\n" +
                "]";

        List<HotpleVO> newList = new ArrayList<>();

        JSONArray arr = new JSONArray(str);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            HotpleVO vo = new HotpleVO();
            vo.setGoId(obj.getString("GOID"));
            vo.setBusnName(obj.getString("BUSNNAME"));
            vo.setGoGrd(obj.getDouble("GOGRD"));
            vo.setGoImg(obj.getString("GOING"));
            vo.setHtAddr(obj.getString("HTADDR"));
            vo.setHtLng(obj.getDouble("HTLNG"));
            vo.setHtLat(obj.getDouble("HTLAT"));
            vo.setHtTel(obj.getString("HTTEL"));
            System.out.println(vo);
        }
    }
}
