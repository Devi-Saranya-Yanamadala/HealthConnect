package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "billing-service")
public interface BillingClient {

    @GetMapping("/api/invoices/revenue/total")
    Double getTotalRevenue();

    // ✅ ADDED
    @GetMapping("/api/invoices/revenue/by-date")
    Double getRevenueByDate(@RequestParam("date") String date);
}