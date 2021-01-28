package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "alliance")
@Entity
@Data
public class AllianceVO {
    @Id
    private String alc_code;

    private String name;
    private String email;
    private String phone;
    private String content;
}
