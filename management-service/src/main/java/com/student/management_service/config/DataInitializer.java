package com.student.management_service.config;

import com.student.management_service.dao.ClassRepository;
import com.student.management_service.dao.TeacherRepository;
import com.student.management_service.entity.ClassEntity;
import com.student.management_service.entity.Teacher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ClassRepository classRepository, TeacherRepository teacherRepository) {
        return args -> {

            System.out.println("Initializing data in MANAGEMENT SERVICE");

            // Create a default teacher and class if it doesn't exist
            for (int i = 1; i <= 2; i++) {
                var username = "teacher" + (i);
                if (!teacherRepository.existsById((long) i)) {
                    Teacher teacher = new Teacher();

                    teacher.setName("Teacher " + i);
                    teacher.setDob(LocalDate.of(1980 + i, 1, 1));
                    teacher.setEmail("teacher" + i + "@example.com");
                    teacher.setAddress("Address " + i);
                    teacher = teacherRepository.save(teacher);

                    System.out.println("Teacher user created - " + username);

                    if (!classRepository.existsById((long) i)) {
                        ClassEntity schoolClass = new ClassEntity();
                        schoolClass.setClassName("Class " + i);
                        schoolClass.setTeacher(teacher);
                        classRepository.save(schoolClass);
                        System.out.println("Class created - Class " + i);
                    }
                }
            }

        };
    }
} 