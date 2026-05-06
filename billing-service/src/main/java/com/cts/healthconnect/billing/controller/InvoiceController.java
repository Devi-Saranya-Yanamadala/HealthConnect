package com.cts.healthconnect.billing.controller;

import com.cts.healthconnect.billing.dto.*;
import com.cts.healthconnect.billing.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    @PostMapping
    public InvoiceResponseDto generate(@Valid @RequestBody InvoiceRequestDto dto) {
        return service.generateInvoice(dto);
    }

    @GetMapping("/{invoiceNumber}")
    public InvoiceResponseDto get(@PathVariable String invoiceNumber) {
        return service.getInvoice(invoiceNumber);
    }

    @PutMapping("/{invoiceNumber}/pay")
    public void pay(@PathVariable String invoiceNumber) {
        service.markInvoicePaid(invoiceNumber);
    }

    @GetMapping("/revenue/total")
    public Double getTotalRevenue() {
        return service.getTotalRevenue();
    }
}