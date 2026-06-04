package com.hospital.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillRequest {
    @NotNull
    private Long appointmentId;
    private Double consultationFee;
    private Double medicineCharge;
    private Double testCharge;
    private Double otherCharge;
    private String paymentMethod;
}
