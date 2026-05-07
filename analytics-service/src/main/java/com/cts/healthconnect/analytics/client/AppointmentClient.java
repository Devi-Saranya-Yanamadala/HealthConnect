package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appointment-service")
public interface AppointmentClient {

    @GetMapping("/api/appointments/count")
    Long getTotalAppointments();

    // ✅ ADDED
    @GetMapping("/api/appointments/count/by-date")
    Long getAppointmentsByDate(@RequestParam("date") String date);
}