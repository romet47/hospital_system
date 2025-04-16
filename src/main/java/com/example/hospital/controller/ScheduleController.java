package com.example.hospital.controller;

import com.example.hospital.entity.Schedule;
import com.example.hospital.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/available")
    public List<Schedule> getAvailableSchedules(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return scheduleService.getAvailableSchedules(startDate, endDate);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Schedule> getSchedulesByDoctor(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return scheduleService.getSchedulesByDoctorAndDate(doctorId, startDate, endDate);
    }
}