package com.student.management_service.service;

import com.student.management_service.dao.ClassRepository;
import com.student.management_service.dto.ClassDto;
import com.student.management_service.entity.ClassEntity;
import com.student.management_service.entity.Teacher;
import com.student.management_service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TeacherService teacherService;

    public ClassEntity getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));
    }

    public boolean doesClassExistById(Long id) {
        var classExists = classRepository.existsById(id);
        if (!classExists) {
            throw new ResourceNotFoundException("Class not found with id: " + id);
        }
        return true;
    }

    public ClassEntity createClass(ClassDto cls) {
        ClassEntity classEntity = new ClassEntity();

        classEntity.setClassId(cls.getClassId());
        classEntity.setClassName(cls.getClassName());

        Teacher tch = teacherService.getTeacherById(cls.getTeacher());
        classEntity.setTeacher(tch);

        return classRepository.save(classEntity);
    }

}

