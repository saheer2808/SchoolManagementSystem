package com.student.student_service.dao;

import com.student.student_service.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find students by classId
    List<Student> findByClassId(Long classId);
}