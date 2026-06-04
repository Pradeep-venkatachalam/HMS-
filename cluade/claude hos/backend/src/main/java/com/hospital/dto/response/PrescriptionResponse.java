package com.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String appointmentDate;
    private String medications;
    private String diagnosis;
    private String instructions;
    private String followUpDate;
    private String createdAt;
}
