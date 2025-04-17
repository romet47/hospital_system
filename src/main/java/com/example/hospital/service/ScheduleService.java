package com.example.hospital.service;

import com.example.hospital.entity.Schedule;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    // 添加构造器注入
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
    }

    public List<Schedule> getAvailableSchedules(Date startDate, Date endDate) {
        return scheduleRepository.findByWorkDateBetweenAndStatus(startDate, endDate, 1);
    }

    public List<Schedule> getSchedulesByDoctorAndDate(Long doctorId, Date startDate, Date endDate) {
        return scheduleRepository.findByDoctorIdAndWorkDateBetween(doctorId, startDate, endDate);
    }

    // 添加saveSchedule方法
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}