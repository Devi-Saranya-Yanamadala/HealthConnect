package com.cts.healthconnect.doctor.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PrescriptionResponseDto {

    private Long   id;
    private String prescriptionCode;
    private String appointmentCode;
    private String patientCode;
    private String doctorCode;
    private String diagnosis;
    private String medicinesJson;
    private String notes;
    private String followUpDate;
    private LocalDateTime createdAt;
}