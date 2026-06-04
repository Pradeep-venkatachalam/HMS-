package com.hospital.service.impl;

import com.hospital.dto.request.PrescriptionRequest;
import com.hospital.dto.response.PrescriptionResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Prescription;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.PrescriptionRepository;
import com.hospital.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Prescription prescription = Prescription.builder()
            .appointment(appointment)
            .medications(request.getMedications())
            .diagnosis(request.getDiagnosis())
            .instructions(request.getInstructions())
            .followUpDate(request.getFollowUpDate())
            .build();

        return toResponse(prescriptionRepository.save(prescription));
    }

    @Override
    @Transactional
    public PrescriptionResponse updatePrescription(Long id, PrescriptionRequest request) {
        Prescription prescription = prescriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        if (request.getMedications() != null) prescription.setMedications(request.getMedications());
        if (request.getDiagnosis() != null) prescription.setDiagnosis(request.getDiagnosis());
        if (request.getInstructions() != null) prescription.setInstructions(request.getInstructions());
        if (request.getFollowUpDate() != null) prescription.setFollowUpDate(request.getFollowUpDate());

        return toResponse(prescriptionRepository.save(prescription));
    }

    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {
        return toResponse(prescriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found")));
    }

    @Override
    public PrescriptionResponse getPrescriptionByAppointment(Long appointmentId) {
        return toResponse(prescriptionRepository.findByAppointmentId(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found")));
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByAppointmentPatientId(patientId)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByAppointmentDoctorId(doctorId)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponse> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private PrescriptionResponse toResponse(Prescription p) {
        Appointment a = p.getAppointment();
        return PrescriptionResponse.builder()
            .id(p.getId())
            .appointmentId(a.getId())
            .patientName(a.getPatient().getFirstName() + " " + a.getPatient().getLastName())
            .doctorName("Dr. " + a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName())
            .appointmentDate(a.getAppointmentDate().toString())
            .medications(p.getMedications())
            .diagnosis(p.getDiagnosis())
            .instructions(p.getInstructions())
            .followUpDate(p.getFollowUpDate())
            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toString() : null)
            .build();
    }
}
