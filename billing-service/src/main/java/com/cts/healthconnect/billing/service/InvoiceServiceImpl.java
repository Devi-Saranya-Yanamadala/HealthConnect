package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.client.AuditClient;
import com.cts.healthconnect.billing.dto.InvoiceRequestDto;
import com.cts.healthconnect.billing.dto.InvoiceResponseDto;
import com.cts.healthconnect.billing.entity.Invoice;
import com.cts.healthconnect.billing.entity.InvoiceStatus;
import com.cts.healthconnect.billing.exception.InvoiceNotFoundException;
import com.cts.healthconnect.billing.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
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
                .invoiceNumber(UUID.randomUUID().toString())
                .appointmentCode(dto.getAppointmentCode())
                .patientId(dto.getPatientId())
                .doctorCode(dto.getDoctorCode())
                .totalAmount(dto.getTotalAmount())
                .paidAmount(dto.getPaidAmount())
                .paymentMode(dto.getPaymentMode())
                .status(InvoiceStatus.GENERATED)
                .build();

        repository.save(invoice);

        // ✅ AUDIT LOG
        audit("GENERATE_INVOICE", invoice.getInvoiceNumber(),
              "Patient: " + dto.getPatientId()
              + " | Amount: Rs." + dto.getTotalAmount());

        return map(invoice);
    }

    @Override
    public InvoiceResponseDto getInvoice(String invoiceNumber) {
        Invoice invoice = repository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceNumber));
        return map(invoice);
    }

    @Override
    public void markInvoicePaid(String invoiceNumber) {
        Invoice invoice = repository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceNumber));
        invoice.setStatus(InvoiceStatus.PAID);

        // ✅ AUDIT LOG
        audit("PAY_INVOICE", invoiceNumber,
              "Invoice marked as paid | Amount: Rs." + invoice.getTotalAmount());
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

    // ✅ Audit helper
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

    private InvoiceResponseDto map(Invoice invoice) {
        return InvoiceResponseDto.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .appointmentCode(invoice.getAppointmentCode())
                .totalAmount(invoice.getTotalAmount())
                .paidAmount(invoice.getPaidAmount())
                .status(invoice.getStatus().name())
                .build();
    }
}