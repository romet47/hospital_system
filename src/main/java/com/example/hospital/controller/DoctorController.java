package com.example.hospital.controller;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Schedule;
import com.example.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/doctors", "/api/admin/doctors"})
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{doctorId}/schedules")
    public ResponseEntity<?> getSchedules(@PathVariable Long doctorId) {
        if (doctorId == null) {
            return ResponseEntity.badRequest().body("doctorId 不能为空");
        }
        List<Schedule> schedules = doctorService.getSchedulesByDoctorId(doctorId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/api/admin/doctors") // 完整路径
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorService.getAllDoctors(pageable);
    }

    @GetMapping("/department/{departmentId}")
    public List<Doctor> getDoctorsByDepartment(@PathVariable Long departmentId) {
        return doctorService.getDoctorsByDepartment(departmentId);
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }
}
