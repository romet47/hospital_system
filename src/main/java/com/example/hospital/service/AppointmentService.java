package com.example.hospital.service;

import com.example.hospital.entity.Appointment;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Schedule;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.AppointmentRepository;
import com.example.hospital.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;

    // 添加构造器注入
    public AppointmentService(AppointmentRepository appointmentRepository,
                              ScheduleRepository scheduleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public Appointment createAppointment(Patient patient, Schedule schedule) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setSchedule(schedule);
        appointment.setDoctor(schedule.getDoctor());
        appointment.setDepartment(schedule.getDepartment());
        appointment.setAppointmentTime(new Date());
        appointment.setStatus("PENDING");

        // 更新可用号源
        schedule.setAvailableNumber(schedule.getAvailableNumber() - 1);
        scheduleRepository.save(schedule);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional
    public Appointment cancelAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findByIdAndPatientId(appointmentId, patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!"PENDING".equals(appointment.getStatus())) {
            throw new IllegalStateException("Only pending appointments can be cancelled");
        }

        appointment.setStatus("CANCELLED");

        // 释放号源
        Schedule schedule = appointment.getSchedule();
        schedule.setAvailableNumber(schedule.getAvailableNumber() + 1);
        scheduleRepository.save(schedule);

        return appointmentRepository.save(appointment);
    }
}