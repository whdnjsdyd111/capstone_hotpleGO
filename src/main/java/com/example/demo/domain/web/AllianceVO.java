package com.example.demo.domain.web;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AllianceVO {
    private String alcCode;
    private String name;
    private String email;
    private String phone;
    private String content;

}
