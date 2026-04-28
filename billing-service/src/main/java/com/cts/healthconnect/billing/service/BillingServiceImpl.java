package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.dto.*;
import com.cts.healthconnect.billing.entity.*;
import com.cts.healthconnect.billing.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository repository;

    // ✅ CREATE INVOICE (fixes nulls)
    @Override
    public InvoiceResponseDto createInvoice(
            String appointmentCode,
            Long patientId,
            String doctorCode,
            Double totalAmount) {

        Invoice invoice = Invoice.builder()
                .invoiceNumber(UUID.randomUUID().toString())
                .appointmentCode(appointmentCode)
                .patientId(patientId)
                .doctorCode(doctorCode)
                .totalAmount(totalAmount)
                .paidAmount(0.0)
                .paymentMode("CASH")
                .status(InvoiceStatus.GENERATED)
                .build();

        Invoice saved = repository.save(invoice);
        return mapToResponse(saved);
    }

    // ✅ PATCH PAYMENT STATUS
    @Override
    public InvoiceResponseDto updatePaymentStatus(
            Long invoiceId,
            InvoiceStatusUpdateRequestDto dto) {

        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found: " + invoiceId));

        invoice.setStatus(dto.getStatus());
        repository.save(invoice);

        return mapToResponse(invoice);
    }

    private InvoiceResponseDto mapToResponse(Invoice invoice) {
        return InvoiceResponseDto.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .appointmentCode(invoice.getAppointmentCode())
                .totalAmount(invoice.getTotalAmount())
                .paidAmount(invoice.getPaidAmount())
                .status(invoice.getStatus().name())
                .build();
    }
}