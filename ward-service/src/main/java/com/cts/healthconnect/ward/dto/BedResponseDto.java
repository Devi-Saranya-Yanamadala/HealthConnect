package com.cts.healthconnect.ward.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BedResponseDto {
    private String bedNumber;
    private String wardType;
    private String status; // "AVAILABLE" or "OCCUPIED"
}