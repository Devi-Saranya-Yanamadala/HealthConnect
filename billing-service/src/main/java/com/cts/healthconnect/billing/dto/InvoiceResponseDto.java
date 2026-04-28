package com.cts.healthconnect.billing.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InvoiceResponseDto {

    private String invoiceNumber;

    private String appointmentCode;

    private Double totalAmount;
    private Double paidAmount;

    private String status;
}