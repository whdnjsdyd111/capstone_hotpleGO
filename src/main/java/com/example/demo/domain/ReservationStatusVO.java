package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationStatusVO {
    private String riCode;
    private String meCode;
    private int rsMeNum;
    private String uCode;
}
