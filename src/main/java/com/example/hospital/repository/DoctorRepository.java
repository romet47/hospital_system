package com.example.hospital.repository;

import com.example.hospital.entity.Doctor;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByDepartmentId(Long departmentId);
    Page<Doctor> findAll(Pageable pageable); // 继承自JpaRepository
    Optional<Doctor> findByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM Doctor d WHERE d.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // 解除关联方案
    @Modifying
    @Query("UPDATE Doctor d SET d.user = null WHERE d.user.id = :userId")
    void updateSetUserNull(@Param("userId") Long userId);
}