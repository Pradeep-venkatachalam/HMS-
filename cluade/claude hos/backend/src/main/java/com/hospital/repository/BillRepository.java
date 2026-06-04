package com.hospital.repository;

import com.hospital.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByAppointmentId(Long appointmentId);
    List<Bill> findByPaymentStatus(String paymentStatus);
    List<Bill> findByAppointmentPatientId(Long patientId);
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.paymentStatus = 'PAID'")
    Double getTotalRevenue();
    @Query("SELECT SUM(b.paidAmount) FROM Bill b")
    Double getTotalCollected();
}
