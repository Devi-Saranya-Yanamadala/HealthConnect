package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.dto.InvoiceResponseDto;
import com.cts.healthconnect.billing.dto.InvoiceStatusUpdateRequestDto;

/**
 * Billing service contract for invoice operations
 */
public interface BillingService {

    /**
     * Updates the payment status of an invoice.
     * Used by PATCH /api/v1/invoices/{invoiceId}/payment-status
     *
     * @param invoiceId the invoice ID
     * @param dto the new invoice status
     * @return updated invoice details
     */
    InvoiceResponseDto updatePaymentStatus(
            Long invoiceId,
            InvoiceStatusUpdateRequestDto dto
    );

	InvoiceResponseDto createInvoice(String appointmentCode, Long patientId, String doctorCode, Double totalAmount);

}