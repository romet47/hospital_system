package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Schedule;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, ScheduleRepository scheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // 原有方法
    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // 新增的updateDoctor方法
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        return doctorRepository.findById(id)
                .map(existingDoctor -> {
                    existingDoctor.setName(doctorDetails.getName());

                    // 确保department不为null
                    if (doctorDetails.getDepartment() == null) {
                        throw new IllegalArgumentException("Department cannot be null");
                    }
                    existingDoctor.setDepartment(doctorDetails.getDepartment());

                    existingDoctor.setTitle(doctorDetails.getTitle());
                    existingDoctor.setSpecialty(doctorDetails.getSpecialty());
                    existingDoctor.setIntroduction(doctorDetails.getIntroduction());
                    return doctorRepository.save(existingDoctor);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    // 新增的deleteDoctor方法
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    // 新增：获取所有医生（分页）
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    // 新增：根据 doctorId 获取排班信息
    public List<Schedule> getSchedulesByDoctorId(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId);
    }
}
