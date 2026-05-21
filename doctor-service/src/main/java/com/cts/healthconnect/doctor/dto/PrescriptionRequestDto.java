package com.cts.healthconnect.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrescriptionRequestDto {

    @NotBlank(message = "Appointment code is required")
    private String appointmentCode;

    @NotBlank(message = "Patient code is required")
    private String patientCode;

    @NotBlank(message = "Doctor code is required")
    private String doctorCode;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    // JSON array string sent from frontend:
    // [{"name":"Paracetamol","dosage":"500mg","frequency":"Twice daily","duration":"5 days","instructions":"After food"}]
    private String medicinesJson;

    private String notes;

    private String followUpDate;   // "2025-07-15"
}