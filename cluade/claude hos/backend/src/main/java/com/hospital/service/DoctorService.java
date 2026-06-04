package com.hospital.service;

import com.hospital.dto.request.DoctorRequest;
import com.hospital.dto.response.DoctorResponse;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    List<DoctorResponse> searchDoctors(String name);
    List<DoctorResponse> getDoctorsBySpecialization(String specialization);
    void deleteDoctor(Long id);
    DoctorResponse getDoctorByUserId(Long userId);
}
