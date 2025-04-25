package com.example.hospital.controller;

import com.example.hospital.dto.LoginRequest;
import com.example.hospital.dto.LoginResponse;
import com.example.hospital.dto.ResetPasswordRequest;
import com.example.hospital.entity.User;
import com.example.hospital.exception.ResourceNotFoundException;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.EmailService;
import com.example.hospital.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    // 只注入认证相关的依赖
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 调试：打印输入的密码和用户名
            System.out.println("Login attempt - Username: " + loginRequest.getUsername());
            System.out.println("Login attempt - Password: " + loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());

            return ResponseEntity.ok(Map.of("token", jwt, "success", true));
        } catch (Exception e) {
            // 打印具体错误
            System.err.println("Login failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        // 1. 验证验证码
        if (!emailService.verifyCode(request.getEmail(), request.getCode())) {
            return ResponseEntity.badRequest().body("验证码错误或已过期");
        }

        // 2. 查找用户
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        // 3. 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 4. 可选：使之前的token失效

        return ResponseEntity.ok("密码重置成功");
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, @RequestParam String code) {
        // 验证验证码
        if (!emailService.verifyCode(user.getEmail(), code)) {
            return ResponseEntity.badRequest().body("验证码错误或已过期");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole("PATIENT"); // 默认注册为患者

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ResponseEntity.badRequest().body("邮箱格式不正确");
        }

        try {
            // 移除邮箱是否已注册的检查
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok("验证码已发送");
        } catch (Exception e) {
            System.err.println("发送验证码失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("发送验证码失败: " + e.getMessage());
        }
    }
}