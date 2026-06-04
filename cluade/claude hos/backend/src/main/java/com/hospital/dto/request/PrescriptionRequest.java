package com.hospital.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionRequest {
    @NotNull
    private Long appointmentId;
    private String medications;
    private String diagnosis;
    private String instructions;
    private String followUpDate;
}
