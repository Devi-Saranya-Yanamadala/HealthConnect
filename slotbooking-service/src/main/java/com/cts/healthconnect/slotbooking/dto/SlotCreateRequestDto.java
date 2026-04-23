package com.cts.healthconnect.slotbooking.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotCreateRequestDto {

    private String doctorCode;
    private LocalDate slotDate;

    private LocalTime startTime;
    private LocalTime endTime;

    // in minutes (e.g., 15, 30)
    private int slotDuration;
}
