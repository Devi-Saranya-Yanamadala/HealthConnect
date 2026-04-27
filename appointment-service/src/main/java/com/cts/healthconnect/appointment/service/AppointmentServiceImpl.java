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

    @Override
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto dto) {

        // ✅ RULE ENFORCEMENT
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

        // ✅ Only now book the slot
        slotClient.bookSlot(dto.getSlotId());

        Appointment appointment = Appointment.builder()
                .appointmentCode(UUID.randomUUID().toString())
                .patientId(dto.getPatientId())
                .doctorCode(dto.getDoctorCode())
                .slotId(dto.getSlotId())
                .appointmentDate(dto.getAppointmentDate())
                .status(AppointmentStatus.BOOKED)
                .build();

        Appointment saved = repository.save(appointment);
        return mapToResponse(saved);
    }
    @Override
    public void cancelAppointment(String appointmentCode) {

        Appointment appointment = repository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException("Only BOOKED appointments can be cancelled");
        }

        slotClient.releaseSlot(appointment.getSlotId());
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    @Override
    public AppointmentResponseDto rescheduleAppointment(AppointmentRescheduleRequestDto dto) {

        Appointment appointment = repository.findByAppointmentCode(dto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentNotFoundException(dto.getAppointmentCode()));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException("Only BOOKED appointments can be rescheduled");
        }

        Long oldSlotId = appointment.getSlotId();

        slotClient.releaseSlot(oldSlotId);

        try {
            slotClient.bookSlot(dto.getNewSlotId());
        } catch (Exception e) {
            slotClient.bookSlot(oldSlotId); // rollback safety
            throw e;
        }

        appointment.setSlotId(dto.getNewSlotId());
        appointment.setAppointmentDate(dto.getNewAppointmentDate());

        return mapToResponse(appointment);
    }

    @Override
    public void completeAppointment(String appointmentCode) {

        Appointment appointment = repository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException("Only BOOKED appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
    }
    
    

    @Override
    public Long getTotalAppointments() {
        return repository.count();
    }


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
