package com.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BillResponse {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String appointmentDate;
    private Double consultationFee;
    private Double medicineCharge;
    private Double testCharge;
    private Double otherCharge;
    private Double totalAmount;
    private Double paidAmount;
    private String paymentStatus;
    private String paymentMethod;
    private String createdAt;
    private String paidAt;
}
