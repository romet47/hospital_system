package com.example.hospital.service;

import com.example.hospital.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class StatsService {
    private final AppointmentRepository appointmentRepository;

    public StatsService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Map<String, Long> getAppointmentStatsByDepartment(Date startDate, Date endDate) {
        return appointmentRepository.countAppointmentsByDepartment(startDate, endDate);
    }

    public Map<String, Long> getAppointmentStatsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }
}