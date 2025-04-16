package com.example.hospital.controller;

import com.example.hospital.service.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/department")
    public Map<String, Long> getStatsByDepartment(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return statsService.getAppointmentStatsByDepartment(startDate, endDate);
    }

    @GetMapping("/status")
    public Map<String, Long> getStatsByStatus() {
        return statsService.getAppointmentStatsByStatus();
    }
}