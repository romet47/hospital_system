package com.example.hospital.repository;

import com.example.hospital.entity.Appointment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, Date start, Date end);
    List<Appointment> findByDepartmentIdAndAppointmentTimeBetween(Long departmentId, Date start, Date end);
    List<Appointment> findByStatus(String status);
    @Query("SELECT a.department.name, COUNT(a) FROM Appointment a " +
            "WHERE a.appointmentTime BETWEEN :startDate AND :endDate " +
            "GROUP BY a.department.name")
    Map<String, Long> countAppointmentsByDepartment(@Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    Map<String, Long> countAppointmentsByStatus();

    Optional<Appointment> findByIdAndPatientId(Long id, Long patientId);
}