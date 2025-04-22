package com.example.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;

    public void sendVerificationCode(String email) {
        try {
            String code = RandomStringUtils.randomNumeric(6);
            System.out.println("尝试发送验证码到: " + email);

            // 存储到Redis
            redisTemplate.opsForValue().set(
                    "verification_code:" + email,
                    code,
                    5, TimeUnit.MINUTES
            );
            System.out.println("验证码已存储到Redis");

            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1031008550@qq.com");
            message.setTo(email);
            message.setSubject("医院预约系统验证码");
            message.setText("您的验证码是: " + code + "，5分钟内有效");

            mailSender.send(message);
            System.out.println("邮件发送成功");
        } catch (Exception e) {
            System.err.println("邮件发送失败: " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常以便Controller捕获
        }
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get("verification_code:" + email);
        return code.equals(storedCode);
    }


}