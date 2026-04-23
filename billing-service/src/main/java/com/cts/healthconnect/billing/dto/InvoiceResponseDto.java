package com.cts.healthconnect.billing.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceResponseDto {

    private String invoiceNumber;
    private String appointmentCode;
    private Double totalAmount;
    private Double paidAmount;
    private String status;
}

