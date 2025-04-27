package com.example.hospital.service;

import com.example.hospital.dto.ScheduleRequest;
import com.example.hospital.entity.Department;
import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Schedule;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public ScheduleService(
            ScheduleRepository scheduleRepository,
            DoctorRepository doctorRepository,          // 新增
            DepartmentRepository departmentRepository   // 新增
    ) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }
    public Schedule createSchedule(ScheduleRequest request) {
        // 1. 校验医生和科室是否存在
        Doctor doctor = doctorRepository.findById(request.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("医生不存在"));
        Department department = departmentRepository.findById(request.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("科室不存在"));

        // 2. 检查是否已有重复排班
        if (scheduleRepository.existsByDoctorIdAndWorkDateAndTimeSlot(
                doctor.getId(), request.getWorkDate(), request.getTimeSlot())) {
            throw new IllegalArgumentException("该医生在此时间段已有排班");
        }

        // 3. 创建排班
        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setDepartment(department);
        schedule.setWorkDate(request.getWorkDate());
        schedule.setTimeSlot(request.getTimeSlot());
        schedule.setTotalNumber(request.getTotalNumber());
        schedule.setAvailableNumber(request.getAvailableNumber());
        schedule.setStatus(request.getStatus());

        return scheduleRepository.save(schedule);
    }
    // 获取单个排班
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("排班未找到，ID: " + id));
    }


    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


    // 获取可用排班
    public List<Schedule> getAvailableSchedules(Date startDate, Date endDate) {
        return scheduleRepository.findByWorkDateBetweenAndStatus(startDate, endDate, 1);
    }

    // 获取医生排班
    public List<Schedule> getSchedulesByDoctorAndDate(Long doctorId, Date startDate, Date endDate) {
        return scheduleRepository.findByDoctorIdAndWorkDateBetween(doctorId, startDate, endDate);
    }

    // 保存排班
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // 更新排班
    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        Schedule schedule = getScheduleById(id);
        schedule.setDoctor(scheduleDetails.getDoctor());       // 修改这里
        schedule.setDepartment(scheduleDetails.getDepartment()); // 修改这里
        schedule.setWorkDate(scheduleDetails.getWorkDate());
        schedule.setTimeSlot(scheduleDetails.getTimeSlot());
        schedule.setTotalNumber(scheduleDetails.getTotalNumber());
        schedule.setAvailableNumber(scheduleDetails.getAvailableNumber());
        schedule.setStatus(scheduleDetails.getStatus());
        return scheduleRepository.save(schedule);
    }

    // 删除排班
    public void deleteSchedule(Long id) {
        Schedule schedule = getScheduleById(id);
        scheduleRepository.delete(schedule);
    }

    // 获取所有排班（分页）
    public Page<Schedule> getAllSchedules(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }


}