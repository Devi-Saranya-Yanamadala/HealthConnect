package com.cts.healthconnect.billing.repository;



import com.cts.healthconnect.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i")
    Double getTotalRevenue();

}
