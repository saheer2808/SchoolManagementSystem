package com.student.management_service.controller;


import com.student.management_service.dto.ClassDto;
import com.student.management_service.entity.ClassEntity;
import com.student.management_service.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@Valid @RequestBody ClassDto cls) {
        ClassEntity createdClass = classService.createClass(cls);
        return new ResponseEntity<>(createdClass, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassEntity> getClassById(@PathVariable Long id) {
        ClassEntity classEntity = classService.getClassById(id);
        return new ResponseEntity<>(classEntity, HttpStatus.OK);
    }

    @GetMapping("/exist/{id}")
    public ResponseEntity<Boolean> doesClassExistById(@PathVariable Long id) {
        Boolean classExists = classService.doesClassExistById(id);
        return new ResponseEntity<>(classExists, HttpStatus.OK);
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<Long> getClassTeacherId(@PathVariable Long id) {
        Long teacherId = classService.getClassTeacherId(id);
        return new ResponseEntity<>(teacherId, HttpStatus.OK);
    }

}