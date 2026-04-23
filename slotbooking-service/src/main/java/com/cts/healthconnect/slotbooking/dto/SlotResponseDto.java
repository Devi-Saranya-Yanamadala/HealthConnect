package com.cts.healthconnect.slotbooking.dto;



import com.cts.healthconnect.slotbooking.entity.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class SlotResponseDto {

    private Long slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;
}
