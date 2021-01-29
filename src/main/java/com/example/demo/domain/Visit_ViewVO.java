package com.example.demo.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Table(name = "visit_view")
@Entity
@Data
@ToString
public class Visit_ViewVO {
    @Id
    @Column(name ="visit_date")
    private String visitDate;

    @NotBlank
    private Integer visitNum;

    @NotBlank
    private Integer viewNum;




}
