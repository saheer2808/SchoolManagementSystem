package com.student.student_service.controller;

import com.student.student_service.dao.UserRepository;
import com.student.student_service.dto.JwtResponse;
import com.student.student_service.dto.LoginRequest;
import com.student.student_service.dto.SignupRequest;
import com.student.student_service.entity.Role;
import com.student.student_service.entity.User;
import com.student.student_service.exception.ConstraintViolationException;
import com.student.student_service.exception.InsufficientAuthenticationException;
import com.student.student_service.security.jwt.JwtUtils;
import com.student.student_service.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        if(!userRepository.existsByUsername(loginRequest.getUsername()))
            throw new InsufficientAuthenticationException("User Not Found with username: " + loginRequest.getUsername());
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(item -> item.getAuthority().replace("ROLE_", ""))
                    .orElse("");

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    role,
                    userDetails.getEntityId()));
        }
        catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new InsufficientAuthenticationException("Invalid Password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Create new user
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEntityId(signUpRequest.getEntityId());

        // Set role
        try {
            Role role = Role.valueOf(signUpRequest.getRole().toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: Invalid role specified!");
        }

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException("User Already Exists");
        }

        Map<String, String> response = new LinkedHashMap<String, String>();
        response.put("message", "User Added successfully!");
        response.put("status", String.valueOf(HttpStatus.OK));
        response.put("statusCode", String.valueOf(HttpStatus.OK.value()));

        return ResponseEntity.created(null).body(response.toString());
    }
} 