package com.hospital.service.impl;

import com.hospital.dto.request.LoginRequest;
import com.hospital.dto.request.RegisterRequest;
import com.hospital.dto.response.AuthResponse;
import com.hospital.entity.*;
import com.hospital.exception.BadRequestException;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;
import com.hospital.security.JwtUtil;
import com.hospital.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BadRequestException("User not found"));

        String token = jwtUtil.generateToken(user);
        Long profileId = getProfileId(user);

        return AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .fullName(user.getFullName())
            .role(user.getRole().name())
            .userId(user.getId())
            .profileId(profileId)
            .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .fullName(request.getFullName())
            .role(request.getRole())
            .enabled(true)
            .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .fullName(user.getFullName())
            .role(user.getRole().name())
            .userId(user.getId())
            .build();
    }

    private Long getProfileId(User user) {
        if (user.getRole() == Role.PATIENT) {
            return patientRepository.findByUserId(user.getId()).map(p -> p.getId()).orElse(null);
        } else if (user.getRole() == Role.DOCTOR) {
            return doctorRepository.findByUserId(user.getId()).map(d -> d.getId()).orElse(null);
        }
        return null;
    }
}
