package com.example.hospital.service;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.User;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.repository.DoctorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public UserService(
            UserRepository userRepository,
            DoctorRepository doctorRepository
    ) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userDetails.getUsername());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setPhone(userDetails.getPhone());
                    existingUser.setRole(userDetails.getRole());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        // 方案A：直接删除关联医生（物理删除）
        doctorRepository.deleteByUserId(id);

        // 方案B：解除关联（保留医生记录）
        // doctorRepository.updateSetUserNull(id);

        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}