package com.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String reason;
    private String notes;
    private String createdAt;
    private boolean hasPrescription;
    private boolean hasBill;
}
