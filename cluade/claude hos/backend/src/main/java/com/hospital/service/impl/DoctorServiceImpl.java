package com.hospital.service.impl;

import com.hospital.dto.request.DoctorRequest;
import com.hospital.dto.response.DoctorResponse;
import com.hospital.entity.Doctor;
import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.UserRepository;
import com.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        User user = null;
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username already exists");
            }
            user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "doctor123"))
                .email(request.getEmail())
                .fullName("Dr. " + request.getFirstName() + " " + request.getLastName())
                .role(Role.DOCTOR)
                .enabled(true)
                .build();
            userRepository.save(user);
        }

        Doctor doctor = Doctor.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .specialization(request.getSpecialization())
            .qualification(request.getQualification())
            .licenseNumber(request.getLicenseNumber())
            .consultationFee(request.getConsultationFee())
            .availableDays(request.getAvailableDays())
            .availableTimeStart(request.getAvailableTimeStart())
            .availableTimeEnd(request.getAvailableTimeEnd())
            .active(true)
            .user(user)
            .build();

        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setAvailableDays(request.getAvailableDays());
        doctor.setAvailableTimeStart(request.getAvailableTimeStart());
        doctor.setAvailableTimeEnd(request.getAvailableTimeEnd());

        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return toResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> searchDoctors(String name) {
        return doctorRepository.searchByName(name).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationIgnoreCase(specialization).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    @Override
    public DoctorResponse getDoctorByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
        return toResponse(doctor);
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return DoctorResponse.builder()
            .id(doctor.getId())
            .firstName(doctor.getFirstName())
            .lastName(doctor.getLastName())
            .fullName("Dr. " + doctor.getFirstName() + " " + doctor.getLastName())
            .email(doctor.getEmail())
            .phone(doctor.getPhone())
            .specialization(doctor.getSpecialization())
            .qualification(doctor.getQualification())
            .licenseNumber(doctor.getLicenseNumber())
            .consultationFee(doctor.getConsultationFee())
            .availableDays(doctor.getAvailableDays())
            .availableTimeStart(doctor.getAvailableTimeStart())
            .availableTimeEnd(doctor.getAvailableTimeEnd())
            .active(doctor.isActive())
            .createdAt(doctor.getCreatedAt() != null ? doctor.getCreatedAt().toString() : null)
            .userId(doctor.getUser() != null ? doctor.getUser().getId() : null)
            .build();
    }
}
