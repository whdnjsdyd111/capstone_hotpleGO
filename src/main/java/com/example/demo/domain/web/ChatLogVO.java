package com.example.demo.domain.web;

import lombok.Builder;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
public class ChatLogVO {
    private String chtCode;
    private String chtCont;
    private String uCode;
    private String aName;
}
