package com.cts.healthconnect.doctor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorResponseDto {

    private Long id;
    private String doctorCode;
    private String fullName;
    private String specialization;
    private String department;
    private Boolean active;
}
