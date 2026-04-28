package com.cts.healthconnect.appointment.service;

import com.cts.healthconnect.appointment.client.SlotBookingClient;
import com.cts.healthconnect.appointment.dto.*;
import com.cts.healthconnect.appointment.entity.*;
import com.cts.healthconnect.appointment.exception.AppointmentNotFoundException;
import com.cts.healthconnect.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final SlotBookingClient slotClient;

    // =======================
    // BOOK APPOINTMENT
    // =======================
    @Override
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto dto) {

        boolean alreadyBooked =
                repository.existsByPatientIdAndDoctorCodeAndAppointmentDate(
                        dto.getPatientId(),
                        dto.getDoctorCode(),
                        dto.getAppointmentDate()
                );

        if (alreadyBooked) {
            throw new IllegalStateException(
                    "Patient already has an appointment with this doctor for the selected date"
            );
        }

        slotClient.bookSlot(dto.getSlotId());

        Appointment appointment = Appointment.builder()
                .appointmentCode(UUID.randomUUID().toString())
                .patientId(dto.getPatientId())
                .doctorCode(dto.getDoctorCode())
                .slotId(dto.getSlotId())
                .appointmentDate(dto.getAppointmentDate())
                .status(AppointmentStatus.BOOKED)
                .build();

        return mapToResponse(repository.save(appointment));
    }

    // =======================
    // PATCH → CANCEL
    // =======================
    @Override
    public AppointmentResponseDto cancelAppointmentById(Long appointmentId) {

        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException("Appointment not found"));

        return cancelInternal(appointment);
    }

    private AppointmentResponseDto cancelInternal(Appointment appointment) {

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException(
                    "Only BOOKED appointments can be cancelled");
        }

        slotClient.releaseSlot(appointment.getSlotId());
        appointment.setStatus(AppointmentStatus.CANCELLED);

        repository.save(appointment);
        return mapToResponse(appointment);
    }

    // =======================
    // RESCHEDULE
    // =======================
    @Override
    public AppointmentResponseDto rescheduleAppointment(
            AppointmentRescheduleRequestDto dto) {

        Appointment appointment = repository.findByAppointmentCode(dto.getAppointmentCode())
                .orElseThrow(() ->
                        new AppointmentNotFoundException(dto.getAppointmentCode()));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException(
                    "Only BOOKED appointments can be rescheduled");
        }

        Long oldSlotId = appointment.getSlotId();
        slotClient.releaseSlot(oldSlotId);

        try {
            slotClient.bookSlot(dto.getNewSlotId());
        } catch (Exception e) {
            slotClient.bookSlot(oldSlotId);
            throw e;
        }

        appointment.setSlotId(dto.getNewSlotId());
        appointment.setAppointmentDate(dto.getNewAppointmentDate());

        repository.save(appointment);
        return mapToResponse(appointment);
    }

    // =======================
    // PATCH → COMPLETE ✅
    // =======================
    @Override
    public AppointmentResponseDto completeAppointmentById(Long appointmentId) {

        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException(
                    "Only BOOKED appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        repository.save(appointment);

        return mapToResponse(appointment);
    }

    // =======================
    // COUNT
    // =======================
    @Override
    public Long getTotalAppointments() {
        return repository.count();
    }

    // =======================
    // MAPPER
    // =======================
    private AppointmentResponseDto mapToResponse(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .appointmentCode(appointment.getAppointmentCode())
                .patientId(appointment.getPatientId())
                .doctorCode(appointment.getDoctorCode())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus().name())
                .build();
    }
}