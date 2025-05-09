package com.example.hospital.repository;

import com.example.hospital.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctorIdAndWorkDate(Long doctorId, Date workDate);
    List<Schedule> findByDepartmentIdAndWorkDate(Long departmentId, Date workDate);
    boolean existsByDoctorIdAndWorkDateAndTimeSlot(Long doctorId, Date workDate, String timeSlot);

    @Query("SELECT s FROM Schedule s WHERE s.workDate >= :startDate AND s.workDate <= :endDate AND s.status = 1")
    List<Schedule> findAvailableSchedules(Date startDate, Date endDate);
    List<Schedule> findByDoctorIdAndWorkDateBetween(Long doctorId, Date startDate, Date endDate);

    @Query("SELECT s FROM Schedule s WHERE s.workDate BETWEEN ?1 AND ?2 AND s.status = ?3")
    List<Schedule> findByWorkDateBetweenAndStatus(Date startDate, Date endDate, Integer status);

    List<Schedule> findByDoctorId(Long doctorId);
}