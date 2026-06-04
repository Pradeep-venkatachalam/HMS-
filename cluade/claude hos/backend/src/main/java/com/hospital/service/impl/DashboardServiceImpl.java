package com.hospital.service.impl;

import com.hospital.dto.response.DashboardResponse;
import com.hospital.entity.AppointmentStatus;
import com.hospital.repository.*;
import com.hospital.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    @Override
    public DashboardResponse getDashboardStats() {
        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        long totalAppointments = appointmentRepository.count();
        long todayAppointments = appointmentRepository.findByAppointmentDate(LocalDate.now()).size();
        long pending = appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED);
        long completed = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);
        long cancelled = appointmentRepository.countByStatus(AppointmentStatus.CANCELLED);

        Double totalRevenue = billRepository.getTotalRevenue();
        Double totalCollected = billRepository.getTotalCollected();

        return DashboardResponse.builder()
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalAppointments(totalAppointments)
                .todayAppointments(todayAppointments)
                .pendingAppointments(pending)
                .completedAppointments(completed)
                .cancelledAppointments(cancelled)
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .pendingPayments(totalCollected != null ? totalCollected : 0.0)
                .build();
    }
}
