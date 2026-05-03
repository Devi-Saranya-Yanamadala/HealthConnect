package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.dto.*;
import com.cts.healthconnect.billing.entity.*;
import com.cts.healthconnect.billing.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository repository;

    @Override
    public InvoiceResponseDto createInvoice(
            String appointmentCode,
            Long patientId,
            String doctorCode,
            Double totalAmount) {

        // 1. VALIDATION: Check for null or non-positive amounts
        if (totalAmount == null || totalAmount <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero.");
        }

        // 2. CONVERSION: Convert Double to BigDecimal for the Entity
        BigDecimal amount = BigDecimal.valueOf(totalAmount);

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .appointmentCode(appointmentCode)
                .patientId(patientId)
                .doctorCode(doctorCode)
                .totalAmount(amount)
                .paidAmount(BigDecimal.ZERO) // Use BigDecimal.ZERO for initialization
                .paymentMode("CASH")
                .status(InvoiceStatus.GENERATED)
                .build();

        Invoice saved = repository.save(invoice);
        return mapToResponse(saved);
    }

    @Override
    public InvoiceResponseDto updatePaymentStatus(
            Long invoiceId,
            InvoiceStatusUpdateRequestDto dto) {

        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + invoiceId));

        // 3. VALIDATION: Business State Machine check
        // Prevent changing status if the invoice is already PAID or CANCELLED
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot update status: Invoice is already PAID.");
        }

        // Logic: If updating to PAID, ensure paidAmount is set to totalAmount
        if (dto.getStatus() == InvoiceStatus.PAID) {
            invoice.setPaidAmount(invoice.getTotalAmount());
        }

        invoice.setStatus(dto.getStatus());
        repository.save(invoice);

        return mapToResponse(invoice);
    }

    private InvoiceResponseDto mapToResponse(Invoice invoice) {
        return InvoiceResponseDto.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .appointmentCode(invoice.getAppointmentCode())
                // Convert BigDecimal back to Double for the DTO
                .totalAmount(invoice.getTotalAmount().doubleValue())
                .paidAmount(invoice.getPaidAmount().doubleValue())
                .status(invoice.getStatus().name())
                .build();
    }
}