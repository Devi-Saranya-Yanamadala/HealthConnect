package com.cts.healthconnect.ward.dto;


import com.cts.healthconnect.ward.entity.WardType;
import lombok.Data;

@Data
public class WardAdmissionRequestDto {

    private Long patientId;
    private String doctorCode;
    private WardType wardType;
    private String bedNumber;
}
