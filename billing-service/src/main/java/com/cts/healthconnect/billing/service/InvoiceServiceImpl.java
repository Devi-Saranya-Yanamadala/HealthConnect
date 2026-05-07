package com.cts.healthconnect.billing.service;


import com.cts.healthconnect.billing.client.AuditClient;
import com.cts.healthconnect.billing.dto.InvoiceRequestDto;
import com.cts.healthconnect.billing.dto.InvoiceResponseDto;
import com.cts.healthconnect.billing.entity.Invoice;
import com.cts.healthconnect.billing.entity.InvoiceStatus;

import com.cts.healthconnect.billing.dto.*;
import com.cts.healthconnect.billing.entity.*;
import com.cts.healthconnect.billing.exception.InvoiceNotFoundException;
import com.cts.healthconnect.billing.repository.InvoiceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


import java.math.BigDecimal;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final AuditClient       auditClient;

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


        repository.save(invoice);

        // ✅ AUDIT LOG
        audit("GENERATE_INVOICE", invoice.getInvoiceNumber(),
              "Patient: " + dto.getPatientId()
              + " | Amount: Rs." + dto.getTotalAmount());

        

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


        // ✅ AUDIT LOG
        audit("PAY_INVOICE", invoiceNumber,
              "Invoice marked as paid | Amount: Rs." + invoice.getTotalAmount());

        invoice.setPaidAmount(invoice.getTotalAmount());
        repository.save(invoice);

    }

    @Override
    public Double getTotalRevenue() {
        return repository.getTotalRevenue();
    }


    @Override
    public Double getRevenueByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end   = localDate.atTime(23, 59, 59);
        return repository.sumRevenueByDate(start, end);
    }


    private void audit(String action, String resourceId, String details) {
        try {
            auditClient.log(Map.of(
                "module",      "BILLING",
                "action",      action,
                "performedBy", "system",
                "resourceId",  resourceId != null ? resourceId : "",
                "details",     details != null ? details : ""
            ));
        } catch (Exception e) {
            System.err.println(">>> AUDIT FAILED [BILLING]: " + e.getMessage());
        }
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