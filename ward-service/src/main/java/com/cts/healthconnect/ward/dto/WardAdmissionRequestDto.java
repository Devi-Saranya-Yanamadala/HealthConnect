package com.cts.healthconnect.ward.dto;


import com.cts.healthconnect.ward.entity.WardType;
import lombok.Data;

@Data
public class WardAdmissionRequestDto {

    private String patientCode;
    private String doctorCode;
    private WardType wardType;
    private String bedNumber;
}
