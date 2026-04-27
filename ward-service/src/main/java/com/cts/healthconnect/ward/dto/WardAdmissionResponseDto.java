package com.cts.healthconnect.ward.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WardAdmissionResponseDto {

    private String admissionCode;
    private String patientCode;
    private String doctorCode;
    private String wardType;
    private String bedNumber;
    private String status;
}
