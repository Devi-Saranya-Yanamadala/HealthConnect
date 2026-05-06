package com.cts.healthconnect.slotbooking.dto;



import com.cts.healthconnect.slotbooking.entity.SlotStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class SlotResponseDto {

    private Long slotId;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private SlotStatus status;
}
