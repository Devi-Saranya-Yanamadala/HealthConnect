package com.cts.healthconnect.billing.dto;



import lombok.Data;

@Data
public class InvoiceRequestDto {

    private String appointmentCode;
    private Long patientId;
    private String doctorCode;
    private Double totalAmount;
    private Double paidAmount;
    private String paymentMode;
}
