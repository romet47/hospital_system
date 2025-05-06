package com.example.hospital.controller;

import com.example.hospital.entity.Appointment;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Schedule;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.service.AppointmentService;
import com.example.hospital.service.PatientService;
import com.example.hospital.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final ScheduleService scheduleService;

// 构造函数，用于初始化AppointmentController对象
    public AppointmentController(AppointmentService appointmentService,
                                 PatientService patientService,
                                 ScheduleService scheduleService) {
        // 初始化appointmentService对象
        this.appointmentService = appointmentService;
        // 初始化patientService对象
        this.patientService = patientService;
        // 初始化scheduleService对象
        this.scheduleService = scheduleService;
    }


    @GetMapping("/my")
    // 根据用户名获取用户详细信息
    public List<Appointment> getMyAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        // 根据用户名获取患者信息
        Patient patient = patientService.getPatientByUsername(userDetails.getUsername());
        // 根据患者id获取患者预约信息
        return appointmentService.getPatientAppointments(patient.getId());
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestParam Long scheduleId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Patient patient = patientService.getPatientByUsername(userDetails.getUsername());
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
            }

            Schedule schedule = scheduleService.getScheduleById(scheduleId);
            Appointment appointment = appointmentService.createAppointment(patient, schedule);

            return ResponseEntity.ok(appointment);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating appointment");
        }
    }
}