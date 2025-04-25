package com.example.hospital.repository;

import com.example.hospital.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByUserId(Long userId);
    List<Doctor> findByDepartmentId(Long departmentId);
    Page<Doctor> findAll(Pageable pageable); // 继承自JpaRepository
}