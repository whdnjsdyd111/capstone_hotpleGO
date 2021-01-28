package com.example.demo.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Table(name = "alliance")
@Entity
@Data
@ToString
public class AllianceVO {
    @Id
    @Column(name = "alc_code")
    private String alcCode;

    @Size(max = 30)
    private String name;

    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Size(max = 11)
    private String phone;

    @Size(max = 300)
    private String content;

}
