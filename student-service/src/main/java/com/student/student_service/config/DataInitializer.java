package com.student.student_service.config;

import com.student.student_service.dao.UserRepository;
import com.student.student_service.entity.Role;
import com.student.student_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin user created");
            }

            // Create a default teacher user
            if (!userRepository.existsByUsername("teacher")) {
                User teacher = new User();
                teacher.setUsername("teacher");
                teacher.setPassword(passwordEncoder.encode("teacher123"));
                teacher.setRole(Role.TEACHER);
                teacher.setEntityId(1L); // Assuming class ID 1
                userRepository.save(teacher);
                System.out.println("Teacher user created");
            }

            // Create a default student user
            if (!userRepository.existsByUsername("student")) {
                User student = new User();
                student.setUsername("student");
                student.setPassword(passwordEncoder.encode("student123"));
                student.setRole(Role.STUDENT);
                student.setEntityId(1L); // Assuming student ID 1
                userRepository.save(student);
                System.out.println("Student user created");
            }
        };
    }
} 