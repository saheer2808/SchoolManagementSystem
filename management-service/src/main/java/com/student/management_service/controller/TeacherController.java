package com.student.management_service.controller;

import com.student.management_service.dto.TeacherDto;
import com.student.management_service.entity.Teacher;
import com.student.management_service.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto tch) {
        Teacher createdTeacher = teacherService.createTeacher(tch);
        return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

}
