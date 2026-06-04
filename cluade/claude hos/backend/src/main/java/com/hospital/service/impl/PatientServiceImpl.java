package com.hospital.service.impl;

import com.hospital.dto.request.PatientRequest;
import com.hospital.dto.response.PatientResponse;
import com.hospital.entity.Patient;
import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;
import com.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        User user = null;
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username already exists");
            }
            user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "password123"))
                .email(request.getEmail())
                .fullName(request.getFirstName() + " " + request.getLastName())
                .role(Role.PATIENT)
                .enabled(true)
                .build();
            userRepository.save(user);
        }

        Patient patient = Patient.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth()) : null)
            .gender(request.getGender())
            .address(request.getAddress())
            .bloodGroup(request.getBloodGroup())
            .emergencyContact(request.getEmergencyContact())
            .medicalHistory(request.getMedicalHistory())
            .user(user)
            .build();

        return toResponse(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isBlank()) {
            patient.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        }
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setMedicalHistory(request.getMedicalHistory());

        return toResponse(patientRepository.save(patient));
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return toResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PatientResponse> searchPatients(String name) {
        return patientRepository.searchByName(name).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patientRepository.delete(patient);
    }

    @Override
    public PatientResponse getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        return toResponse(patient);
    }

    private PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
            .id(patient.getId())
            .firstName(patient.getFirstName())
            .lastName(patient.getLastName())
            .fullName(patient.getFirstName() + " " + patient.getLastName())
            .email(patient.getEmail())
            .phone(patient.getPhone())
            .dateOfBirth(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null)
            .gender(patient.getGender())
            .address(patient.getAddress())
            .bloodGroup(patient.getBloodGroup())
            .emergencyContact(patient.getEmergencyContact())
            .medicalHistory(patient.getMedicalHistory())
            .createdAt(patient.getCreatedAt() != null ? patient.getCreatedAt().toString() : null)
            .userId(patient.getUser() != null ? patient.getUser().getId() : null)
            .build();
    }
}
