package com.student.management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TeacherDto {
    private Long tid;
    private String name;
    private LocalDate dob;
    private String email;
    private String address;
}
