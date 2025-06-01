package com.student.student_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("MANAGEMENT-SERVICE")
public interface ManagementFeignClient {

    @GetMapping("api/classes/exist/{id}")
    public ResponseEntity<Boolean> doesClassExistById(@PathVariable Long id);

    @GetMapping("/api/classes/{id}/teacher")
    public ResponseEntity<Long> getClassTeacherId(@PathVariable Long id);
}

