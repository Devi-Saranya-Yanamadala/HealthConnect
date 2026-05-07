package com.cts.healthconnect.billing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.healthconnect.billing.dto.InvoiceRequestDto;
import com.cts.healthconnect.billing.dto.InvoiceResponseDto;
import com.cts.healthconnect.billing.service.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor

public class InvoiceController {

    private final InvoiceService service;

    @PostMapping
    public InvoiceResponseDto generate(@RequestBody InvoiceRequestDto dto) {
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
	
	@GetMapping("/revenue/by-date")
	public Double getRevenueByDate(@RequestParam("date") String date) {
	    return service.getRevenueByDate(date);
	}
}
