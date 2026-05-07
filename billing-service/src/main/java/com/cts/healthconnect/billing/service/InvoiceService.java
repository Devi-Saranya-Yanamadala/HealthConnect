package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.dto.InvoiceRequestDto;
import com.cts.healthconnect.billing.dto.InvoiceResponseDto;

public interface InvoiceService {

    InvoiceResponseDto generateInvoice(InvoiceRequestDto dto);

    InvoiceResponseDto getInvoice(String invoiceNumber);

    void markInvoicePaid(String invoiceNumber);
    
    Double getTotalRevenue();
    Double getRevenueByDate(String date);
}

