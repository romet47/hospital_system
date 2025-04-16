package com.example.hospital.repository;

import com.example.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUserId(Long userId);
    Patient findByIdCard(String idCard);

    @Query("SELECT p FROM Patient p JOIN p.user u WHERE u.username = :username")
    Patient findByUserUsername(@Param("username") String username);
}