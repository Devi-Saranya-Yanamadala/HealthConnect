package com.cts.healthconnect.billing.controller;

import com.cts.healthconnect.billing.dto.*;
import com.cts.healthconnect.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService service;
    
    @PatchMapping("/{invoiceId}/payment-status")
    public ResponseEntity<InvoiceResponseDto> updatePaymentStatus(
            @PathVariable Long invoiceId,
            @RequestBody InvoiceStatusUpdateRequestDto dto) {

        return ResponseEntity.ok(
                service.updatePaymentStatus(invoiceId, dto)
        );
    }
}