package com.student.student_service.config;

import com.student.student_service.dao.StudentRepository;
import com.student.student_service.dao.UserRepository;
import com.student.student_service.entity.Role;
import com.student.student_service.entity.Student;
import com.student.student_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static java.lang.Long.valueOf;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            System.out.println("Initializing data in STUDENT SERVICE");

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
            for (int i = 1; i <= 2; i++) {
                var username = "teacher" + (i);
                if (!userRepository.existsByUsername(username)) {
                    User teacher = new User();
                    teacher.setUsername(username);
                    teacher.setPassword(passwordEncoder.encode("teacher123"));
                    teacher.setRole(Role.TEACHER);
                    teacher.setEntityId((long) i);
                    userRepository.save(teacher);
                    System.out.println("Teacher user created - " + username);
                }
            }

            for (int i = 1; i <= 6; i++) {
                var username= "student" + (i);
                // Create a default student user
                if (!userRepository.existsByUsername(username)) {
                    User student = new User();
                    student.setUsername(username);
                    student.setPassword(passwordEncoder.encode("student123"));
                    student.setRole(Role.STUDENT);
                    student.setEntityId((long) i);
                    userRepository.save(student);
                    System.out.println("Student user created - " + username);
                }
            }

            // Create a default student users
            for (int i = 1; i <= 6; i++) {
                var username = "student" + (i);
                if (!studentRepository.existsById((long) i)) {
                    Student student = new Student();
                    student.setRollNo(i);
                    student.setName("Student " + i);
                    student.setDob(LocalDate.of(2000 + i, 1, 1));
                    student.setEmail("student" + i + "@example.com");
                    student.setPhoneNumber("941856789" + i);
                    student.setAddress("Address " + i);
                    var classId = i<= 3 ? 1 : 2;
                    student.setClassId((long) classId);
                    studentRepository.save(student);
                    System.out.println("Student user created - " + username);
                }
            }

        };
    }
} 