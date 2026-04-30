package com.cts.healthconnect.slotbooking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotCreateRequestDto {

    @NotBlank(message = "Doctor code must not be blank")
    private String doctorCode;

    @NotNull(message = "Slot date must be provided")
    @FutureOrPresent(message = "Slot date must be today or in the future")
    private LocalDate slotDate;

    @NotNull(message = "Start time must be provided")
    private LocalTime startTime;

    @NotNull(message = "End time must be provided")
    private LocalTime endTime;

    @Min(value = 5, message = "Slot duration must be at least 5 minutes")
    @Max(value = 120, message = "Slot duration must not exceed 120 minutes")
    private int slotDuration;
}