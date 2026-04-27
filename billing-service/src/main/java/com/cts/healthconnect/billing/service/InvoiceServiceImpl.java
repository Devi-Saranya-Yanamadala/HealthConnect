package com.cts.healthconnect.billing.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cts.healthconnect.billing.dto.InvoiceRequestDto;
import com.cts.healthconnect.billing.dto.InvoiceResponseDto;
import com.cts.healthconnect.billing.entity.Invoice;
import com.cts.healthconnect.billing.entity.InvoiceStatus;
import com.cts.healthconnect.billing.exception.InvoiceNotFoundException;
import com.cts.healthconnect.billing.repository.InvoiceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

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
    }
    

    @Override
    public Double getTotalRevenue() {
        return repository.getTotalRevenue();
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
