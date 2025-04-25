package com.example.hospital.service;

import com.example.hospital.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {
    private final AppointmentRepository appointmentRepository;

    public StatsService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // 按科室统计
    public Map<String, Long> getAppointmentStatsByDepartment(Date startDate, Date endDate) {
        return appointmentRepository.countAppointmentsByDepartment(startDate, endDate);
    }

    // 按状态统计
    public Map<String, Long> getAppointmentStatsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    // 按日期统计
    public Map<String, Long> getAppointmentStats(Date startDate, Date endDate) {
        // 如果未提供日期参数，设置默认值
        if (startDate == null) {
            startDate = new Date(0); // 1970-01-01
        }
        if (endDate == null) {
            endDate = new Date(); // 当前日期
        }
        return appointmentRepository.countAppointmentsByDateRange(startDate, endDate);
    }

    // 详细科室统计
    public List<Map<String, Object>> getDepartmentStats() {
        return appointmentRepository.countDetailedStatsByDepartment();
    }
}