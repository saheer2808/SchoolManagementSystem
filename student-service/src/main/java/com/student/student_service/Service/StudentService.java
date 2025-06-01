package com.student.student_service.Service;


import com.student.student_service.dao.StudentRepository;
import com.student.student_service.dto.StudentDto;
import com.student.student_service.entity.Student;
import com.student.student_service.exception.InsufficientAuthenticationException;
import com.student.student_service.exception.ResourceNotFoundException;
import com.student.student_service.feign.ManagementFeignClient;
import com.student.student_service.security.service.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ManagementFeignClient managementFeignClient;

    // Create a new student
    public Student createStudent(StudentDto student) {

        Student std = new Student();

        std.setRollNo(student.getRollNo());
        std.setName(student.getName());
        std.setDob(student.getDob());
        std.setAddress(student.getAddress());
        std.setEmail(student.getEmail());
        std.setPhoneNumber(student.getPhoneNumber());

        checkIfClassExists(student.getClassId());
        std.setClassId(student.getClassId());

        return studentRepository.save(std);
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    // Get student by id
    public Student getStudentById(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("");

        if (role.equals("STUDENT") && !userDetails.getEntityId().equals(id)) {
            throw new InsufficientAuthenticationException("Student not authorized to access this resource");
        }

        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        System.out.println("Student : " + student);


        if (role.equals("TEACHER")) {
            ResponseEntity<Long> studentClassTeacherId = managementFeignClient.getClassTeacherId(student.getClassId());
            if(studentClassTeacherId!= null && !studentClassTeacherId.getBody().equals(userDetails.getEntityId())) {
              throw new InsufficientAuthenticationException("Teacher not authorized to access this student");
            }
        }

        return student;
    }
    
    // Get students by class
    public List<Student> getStudentsByClassId(Long classId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("");
        if (role.equals("TEACHER")){
            ResponseEntity<Long> classTeacherId = managementFeignClient.getClassTeacherId(classId);
            if(classTeacherId!= null && !classTeacherId.getBody().equals(userDetails.getEntityId())) {
                throw new InsufficientAuthenticationException("Teacher not authorized to access this student");
            }
        }
        return studentRepository.findByClassId(classId);
    }

    // Update student
    public Student updateStudent(Long id, StudentDto studentDetails) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("");

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        System.out.println("Student : " + student);

        if (role.equals("STUDENT")) {
            if(!userDetails.getEntityId().equals(id)){
                throw new InsufficientAuthenticationException("Student not authorized to update this resource");
            }
        }

        if (role.equals("TEACHER")) {
            ResponseEntity<Long> studentClassTeacherId = managementFeignClient.getClassTeacherId(student.getClassId());
            if (studentClassTeacherId != null && studentClassTeacherId.getBody()!=null && !studentClassTeacherId.getBody().equals(userDetails.getEntityId())) {
                throw new InsufficientAuthenticationException("Teacher not authorized to update this student");
            }
        }

        if (role.equals("ADMIN")){
            student.setRollNo(Objects.nonNull(studentDetails.getRollNo())?studentDetails.getRollNo() : student.getRollNo());
        }

        if (role.equals("ADMIN") || role.equals("TEACHER")) {
            if(Objects.nonNull(studentDetails.getClassId())){
                checkIfClassExists(student.getClassId());
                student.setClassId(studentDetails.getClassId());
            }
        }

        student.setName(Objects.nonNull(studentDetails.getName())?studentDetails.getName() : student.getName());
        student.setDob(Objects.nonNull(studentDetails.getDob())?studentDetails.getDob() : student.getDob());
        student.setAddress(Objects.nonNull(studentDetails.getAddress())?studentDetails.getAddress() : student.getAddress());
        student.setEmail(Objects.nonNull(studentDetails.getEmail())?studentDetails.getEmail() : student.getEmail());
        student.setPhoneNumber(Objects.nonNull(studentDetails.getPhoneNumber())?studentDetails.getPhoneNumber() : student.getPhoneNumber());

        return studentRepository.save(student);
    }
    
    // Delete student
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    private void checkIfClassExists(Long classId) {
        Boolean classExists = managementFeignClient.doesClassExistById(classId).getBody();
        if (classExists!=null && !classExists) {
            throw new ResourceNotFoundException("Class not found with id: " + classId);
        }
    }

    public Long getStudentsCount() {
        return studentRepository.count();
    }
}