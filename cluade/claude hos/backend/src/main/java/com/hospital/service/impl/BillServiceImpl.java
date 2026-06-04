package com.hospital.service.impl;

import com.hospital.dto.request.BillRequest;
import com.hospital.dto.response.BillResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Bill;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.BillRepository;
import com.hospital.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public BillResponse createBill(BillRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        double consultationFee = request.getConsultationFee() != null ? request.getConsultationFee() :
            (appointment.getDoctor().getConsultationFee() != null ? appointment.getDoctor().getConsultationFee() : 0.0);
        double medicineCharge = request.getMedicineCharge() != null ? request.getMedicineCharge() : 0.0;
        double testCharge = request.getTestCharge() != null ? request.getTestCharge() : 0.0;
        double otherCharge = request.getOtherCharge() != null ? request.getOtherCharge() : 0.0;
        double total = consultationFee + medicineCharge + testCharge + otherCharge;

        Bill bill = Bill.builder()
            .appointment(appointment)
            .consultationFee(consultationFee)
            .medicineCharge(medicineCharge)
            .testCharge(testCharge)
            .otherCharge(otherCharge)
            .totalAmount(total)
            .paidAmount(0.0)
            .paymentStatus("PENDING")
            .paymentMethod(request.getPaymentMethod())
            .build();

        return toResponse(billRepository.save(bill));
    }

    @Override
    @Transactional
    public BillResponse updateBill(Long id, BillRequest request) {
        Bill bill = billRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        if (request.getConsultationFee() != null) bill.setConsultationFee(request.getConsultationFee());
        if (request.getMedicineCharge() != null) bill.setMedicineCharge(request.getMedicineCharge());
        if (request.getTestCharge() != null) bill.setTestCharge(request.getTestCharge());
        if (request.getOtherCharge() != null) bill.setOtherCharge(request.getOtherCharge());

        double total = bill.getConsultationFee() + bill.getMedicineCharge() + bill.getTestCharge() + bill.getOtherCharge();
        bill.setTotalAmount(total);

        return toResponse(billRepository.save(bill));
    }

    @Override
    public BillResponse getBillById(Long id) {
        return toResponse(billRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bill not found")));
    }

    @Override
    public BillResponse getBillByAppointment(Long appointmentId) {
        return toResponse(billRepository.findByAppointmentId(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Bill not found")));
    }

    @Override
    public List<BillResponse> getAllBills() {
        return billRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<BillResponse> getBillsByPatient(Long patientId) {
        return billRepository.findByAppointmentPatientId(patientId)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillResponse markAsPaid(Long id, String paymentMethod) {
        Bill bill = billRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        bill.setPaymentStatus("PAID");
        bill.setPaidAmount(bill.getTotalAmount());
        bill.setPaymentMethod(paymentMethod);
        bill.setPaidAt(LocalDateTime.now());
        return toResponse(billRepository.save(bill));
    }

    private BillResponse toResponse(Bill b) {
        Appointment a = b.getAppointment();
        return BillResponse.builder()
            .id(b.getId())
            .appointmentId(a.getId())
            .patientName(a.getPatient().getFirstName() + " " + a.getPatient().getLastName())
            .doctorName("Dr. " + a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName())
            .appointmentDate(a.getAppointmentDate().toString())
            .consultationFee(b.getConsultationFee())
            .medicineCharge(b.getMedicineCharge())
            .testCharge(b.getTestCharge())
            .otherCharge(b.getOtherCharge())
            .totalAmount(b.getTotalAmount())
            .paidAmount(b.getPaidAmount())
            .paymentStatus(b.getPaymentStatus())
            .paymentMethod(b.getPaymentMethod())
            .createdAt(b.getCreatedAt() != null ? b.getCreatedAt().toString() : null)
            .paidAt(b.getPaidAt() != null ? b.getPaidAt().toString() : null)
            .build();
    }
}
