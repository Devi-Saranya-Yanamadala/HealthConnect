package com.cts.healthconnect.billing.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices",
       uniqueConstraints = @UniqueConstraint(columnNames = "invoice_number"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    private String appointmentCode;
    private Long patientId;
    private String doctorCode;

    private Double totalAmount;
    private Double paidAmount;

    private String paymentMode; // CASH, CARD, UPI

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
