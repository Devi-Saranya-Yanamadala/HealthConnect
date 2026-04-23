package com.cts.healthconnect.appointment.dto;



import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentRescheduleRequestDto {

    private String appointmentCode;
    private Long newSlotId;
    private LocalDate newAppointmentDate;
}