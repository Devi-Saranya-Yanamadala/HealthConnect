package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "billing-service")
public interface BillingClient {

    @GetMapping("/api/billing/revenue/total")
    Double getTotalRevenue();
}
