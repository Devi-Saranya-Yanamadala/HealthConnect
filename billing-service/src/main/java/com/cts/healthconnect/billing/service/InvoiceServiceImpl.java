package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.dto.*;
import com.cts.healthconnect.billing.entity.*;
import com.cts.healthconnect.billing.exception.InvoiceNotFoundException;
import com.cts.healthconnect.billing.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    @Override
    public InvoiceResponseDto generateInvoice(InvoiceRequestDto dto) {
        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .appointmentCode(dto.getAppointmentCode())
                .patientId(dto.getPatientId())
                .doctorCode(dto.getDoctorCode())
                .totalAmount(dto.getTotalAmount())
                .paidAmount(dto.getPaidAmount() != null ? dto.getPaidAmount() : BigDecimal.ZERO)
                .paymentMode(dto.getPaymentMode())
                .status(InvoiceStatus.GENERATED)
                .build();

        return mapToResponse(repository.save(invoice));
    }

    @Override
    public InvoiceResponseDto getInvoice(String invoiceNumber) {
        return repository.findByInvoiceNumber(invoiceNumber)
                .map(this::mapToResponse)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceNumber));
    }

    @Override
    public void markInvoicePaid(String invoiceNumber) {
        Invoice invoice = repository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceNumber));

        // Validation: State Guard
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice is already paid.");
        }
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Cannot pay a cancelled invoice.");
        }

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAmount(invoice.getTotalAmount());
        repository.save(invoice);
    }

    @Override
    public Double getTotalRevenue() {
        return repository.getTotalRevenue();
    }

    private InvoiceResponseDto mapToResponse(Invoice invoice) {
        return InvoiceResponseDto.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .appointmentCode(invoice.getAppointmentCode())
                .totalAmount(invoice.getTotalAmount().doubleValue())
                .paidAmount(invoice.getPaidAmount().doubleValue())
                .status(invoice.getStatus().name())
                .build();
    }
}