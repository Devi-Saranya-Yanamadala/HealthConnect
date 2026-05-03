package com.cts.healthconnect.billing.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class InvoiceRequestDto {
    @NotBlank(message = "Appointment code is mandatory")
    private String appointmentCode;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Doctor code is mandatory")
    private String doctorCode;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Paid amount cannot be negative")
    private BigDecimal paidAmount;

    @Pattern(regexp = "^(CASH|CARD|UPI|INSURANCE)$", message = "Invalid payment mode. Allowed: CASH, CARD, UPI, INSURANCE")
    private String paymentMode;
}