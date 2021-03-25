package com.example.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReservationStatusVO extends MenuVO {
    private String riCode;
    private Byte rsMeNum;
    private String uCode;
}
