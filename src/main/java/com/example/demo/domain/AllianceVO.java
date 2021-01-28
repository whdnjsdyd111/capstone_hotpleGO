package com.example.demo.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "alliance")
@Entity
@Data
public class AllianceVO {
    @Id
    @Column(name = "alc_code")
    private String alcCode;

    private String name;
    private String email;
    private String phone;
    private String content;
}
