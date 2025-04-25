package com.example.hospital.repository;

import com.example.hospital.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // 原有方法
    List<Appointment> findByPatientId(Long patientId);
    Optional<Appointment> findByIdAndPatientId(Long id, Long patientId);

    // 新增统计方法
    @Query("SELECT a.department.name, COUNT(a) FROM Appointment a " +
            "WHERE a.appointmentTime BETWEEN :startDate AND :endDate " +
            "GROUP BY a.department.name")
    Map<String, Long> countAppointmentsByDepartment(@Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    Map<String, Long> countAppointmentsByStatus();

    @Query("SELECT new map(FUNCTION('DATE', a.appointmentTime) as date, COUNT(a) as count) " +
            "FROM Appointment a " +
            "WHERE a.appointmentTime BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', a.appointmentTime)")
    Map<String, Long> countAppointmentsByDateRange(@Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);

    @Query("SELECT new map(d.name as department, COUNT(a) as total, " +
            "SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed, " +
            "SUM(CASE WHEN a.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled) " +
            "FROM Appointment a JOIN a.department d " +
            "GROUP BY d.name")
    List<Map<String, Object>> countDetailedStatsByDepartment();
}