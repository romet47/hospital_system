package com.example.hospital.controller;

import com.example.hospital.entity.*;
import com.example.hospital.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorService doctorService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final StatsService statsService;

    public AdminController(DoctorService doctorService,
                           ScheduleService scheduleService,
                           UserService userService,
                           DepartmentService departmentService,
                           StatsService statsService) {
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.statsService = statsService;
    }

    // 用户管理
    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }


    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // 科室管理
    @PostMapping("/departments")
    public Department addDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    @PutMapping("/departments/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/departments/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

    // 医生管理
    @PutMapping("/doctors/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);
    }

    @DeleteMapping("/doctors/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/doctors")
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorService.getAllDoctors(pageable);
    }

    // 排班管理
    @PostMapping("/schedules")
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        return scheduleService.saveSchedule(schedule);
    }

    @PutMapping("/schedules/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(id, schedule);
    }

    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

    // 数据统计
    @GetMapping("/stats/appointments")
    public Map<String, Long> getAppointmentStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;

        try {
            if (startDate != null) {
                start = dateFormat.parse(startDate);
            }
            if (endDate != null) {
                end = dateFormat.parse(endDate);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，请使用yyyy-MM-dd格式");
        }

        return statsService.getAppointmentStats(start, end);
    }

    @GetMapping("/stats/departments")
    public List<Map<String, Object>> getDepartmentStats() {
        return statsService.getDepartmentStats();
    }
}