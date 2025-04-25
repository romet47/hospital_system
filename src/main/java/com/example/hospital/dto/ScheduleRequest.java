package com.example.hospital.dto;

import com.example.hospital.entity.Department;
import com.example.hospital.entity.Doctor;
import lombok.Data;
import java.util.Date;

@Data
public class ScheduleRequest {
    private Doctor doctor;        // 或简化成 private Long doctorId;
    private Department department; // 或 private Long departmentId;
    private Date workDate;
    private String timeSlot;
    private Integer totalNumber;
    private Integer availableNumber;
    private Integer status;
}