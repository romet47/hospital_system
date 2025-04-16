package com.example.hospital.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date workDate;

    @Column(nullable = false)
    private String timeSlot; // 上午/下午

    @Column(nullable = false)
    private Integer totalNumber;

    @Column(nullable = false)
    private Integer availableNumber;

    @Column(nullable = false)
    private Integer status = 1; // 0-停诊, 1-正常
}