package com.hospital.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String email;
    private String phone;
    @NotBlank
    private String specialization;
    private String qualification;
    private String licenseNumber;
    private Double consultationFee;
    private String availableDays;
    private String availableTimeStart;
    private String availableTimeEnd;
    private String username;
    private String password;
}
