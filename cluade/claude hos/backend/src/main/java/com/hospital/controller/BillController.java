package com.hospital.controller;

import com.hospital.dto.request.BillRequest;
import com.hospital.dto.response.ApiResponse;
import com.hospital.dto.response.BillResponse;
import com.hospital.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BillResponse>> create(@Valid @RequestBody BillRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Bill created", billService.createBill(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BillResponse>> update(@PathVariable Long id, @RequestBody BillRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Bill updated", billService.updateBill(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(billService.getBillById(id)));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ApiResponse<BillResponse>> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(ApiResponse.success(billService.getBillByAppointment(appointmentId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BillResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(billService.getAllBills()));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<BillResponse>>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success(billService.getBillsByPatient(patientId)));
    }

    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BillResponse>> markPaid(@PathVariable Long id, @RequestParam(defaultValue = "CASH") String paymentMethod) {
        return ResponseEntity.ok(ApiResponse.success("Payment recorded", billService.markAsPaid(id, paymentMethod)));
    }
}
