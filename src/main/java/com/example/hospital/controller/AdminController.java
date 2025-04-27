package com.example.hospital.controller;

import com.example.hospital.entity.*;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.service.*;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
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
    private final PasswordEncoder passwordEncoder;
    public AdminController(DoctorService doctorService,
                           ScheduleService scheduleService,
                           UserService userService,
                           DepartmentService departmentService,
                           StatsService statsService,
                           PasswordEncoder passwordEncoder) {
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.statsService = statsService;
        this.passwordEncoder = passwordEncoder;
    }

    // 用户管理
    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    // 获取单个用户
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 添加用户（需密码加密）
    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors() || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("用户名、密码、邮箱和角色不能为空");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("无法删除：用户存在关联数据");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("删除失败: " + e.getMessage());
        }
    }
    // 科室管理
    @GetMapping("/departments")  // 添加这个新方法
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
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
    // 获取所有排班
    @GetMapping("/schedules")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    // 根据ID获取排班
    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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


