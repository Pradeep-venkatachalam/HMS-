package com.hospital.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentRequest {
    @NotNull
    private Long patientId;
    @NotNull
    private Long doctorId;
    @NotNull
    private String appointmentDate;
    @NotNull
    private String appointmentTime;
    private String reason;
    private String notes;
}
