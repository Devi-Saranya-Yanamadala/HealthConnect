package com.cts.healthconnect.billing.controller;

import com.cts.healthconnect.billing.dto.InvoiceRequestDto;
import com.cts.healthconnect.billing.dto.InvoiceResponseDto;
import com.cts.healthconnect.billing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    // ✅ Create invoice
    @PostMapping
    public InvoiceResponseDto generate(@RequestBody InvoiceRequestDto dto) {
        return service.generateInvoice(dto);
    }

    // ✅ Get invoice by number
    @GetMapping("/{invoiceNumber}")
    public InvoiceResponseDto get(@PathVariable String invoiceNumber) {
        return service.getInvoice(invoiceNumber);
    }

    // ✅ Mark invoice as paid
    @PutMapping("/{invoiceNumber}/pay")
    public void pay(@PathVariable String invoiceNumber) {
        service.markInvoicePaid(invoiceNumber);
    }

    // ✅ Total revenue — used by analytics reports
    @GetMapping("/revenue/total")
    public Double getTotalRevenue() {
        return service.getTotalRevenue();
    }

    // ✅ ADDED: date-wise revenue — used by analytics calendar
    @GetMapping("/revenue/by-date")
    public Double getRevenueByDate(@RequestParam("date") String date) {
        return service.getRevenueByDate(date);
    }
}