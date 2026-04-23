package com.cts.healthconnect.billing.exception;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String invoiceNumber) {
        super("Invoice not found: " + invoiceNumber);
    }
}
