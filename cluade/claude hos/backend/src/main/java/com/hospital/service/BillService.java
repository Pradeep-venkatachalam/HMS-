package com.hospital.service;

import com.hospital.dto.request.BillRequest;
import com.hospital.dto.response.BillResponse;

import java.util.List;

public interface BillService {
    BillResponse createBill(BillRequest request);
    BillResponse updateBill(Long id, BillRequest request);
    BillResponse getBillById(Long id);
    BillResponse getBillByAppointment(Long appointmentId);
    List<BillResponse> getAllBills();
    List<BillResponse> getBillsByPatient(Long patientId);
    BillResponse markAsPaid(Long id, String paymentMethod);
}
