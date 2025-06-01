package com.student.student_service.security.jwt;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.student.student_service.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        ErrorMessage a = new ErrorMessage(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), null, "Access Denied: " + authException.getMessage(), request.getRequestURI());
        JsonMapper jsonMapper = new JsonMapper();
        String json = jsonMapper.writeValueAsString(a);
        PrintWriter writer = response.getWriter();
        writer.println(json);
    }
} 