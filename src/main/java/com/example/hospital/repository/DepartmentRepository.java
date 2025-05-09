package com.example.hospital.repository;

import com.example.hospital.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByStatus(Integer status);
}