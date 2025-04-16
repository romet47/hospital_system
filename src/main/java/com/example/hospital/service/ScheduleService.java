package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Schedule;
import com.example.hospital.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import com.example.hospital.exception.ResourceNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;


    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
    }
    public List<Schedule> getAvailableSchedules(Date startDate, Date endDate) {
        return scheduleRepository.findAvailableSchedules(startDate, endDate);
    }

    public List<Schedule> getSchedulesByDoctorAndDate(Long doctorId, Date startDate, Date endDate) {
        return scheduleRepository.findByDoctorIdAndWorkDateBetween(doctorId, startDate, endDate);
    }
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

}