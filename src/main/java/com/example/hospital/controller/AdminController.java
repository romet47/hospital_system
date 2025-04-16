package com.example.hospital.controller;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Schedule;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorService doctorService;
    private final ScheduleService scheduleService;

    public AdminController(DoctorService doctorService, ScheduleService scheduleService) {
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
    }

    @PostMapping("/doctors")
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.saveDoctor(doctor);
    }

    @PostMapping("/schedules")
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        return scheduleService.saveSchedule(schedule);
    }
}