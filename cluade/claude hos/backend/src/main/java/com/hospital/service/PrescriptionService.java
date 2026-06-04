package com.hospital.service;

import com.hospital.dto.request.PrescriptionRequest;
import com.hospital.dto.response.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse createPrescription(PrescriptionRequest request);
    PrescriptionResponse updatePrescription(Long id, PrescriptionRequest request);
    PrescriptionResponse getPrescriptionById(Long id);
    PrescriptionResponse getPrescriptionByAppointment(Long appointmentId);
    List<PrescriptionResponse> getPrescriptionsByPatient(Long patientId);
    List<PrescriptionResponse> getPrescriptionsByDoctor(Long doctorId);
    List<PrescriptionResponse> getAllPrescriptions();
}
