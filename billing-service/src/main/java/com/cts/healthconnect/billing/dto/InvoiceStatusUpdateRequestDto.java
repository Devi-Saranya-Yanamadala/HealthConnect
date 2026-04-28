package com.cts.healthconnect.billing.dto;

import com.cts.healthconnect.billing.entity.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating invoice payment status
 * Used in PATCH /api/v1/invoices/{invoiceId}/payment-status
 */
@Getter
@Setter
public class InvoiceStatusUpdateRequestDto {

    @NotNull(message = "Invoice status must not be null")
    private InvoiceStatus status;
}