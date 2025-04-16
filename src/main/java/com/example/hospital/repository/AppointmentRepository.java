package com.example.hospital.repository;

import com.example.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, Date start, Date end);
    List<Appointment> findByDepartmentIdAndAppointmentTimeBetween(Long departmentId, Date start, Date end);
    List<Appointment> findByStatus(String status);
}