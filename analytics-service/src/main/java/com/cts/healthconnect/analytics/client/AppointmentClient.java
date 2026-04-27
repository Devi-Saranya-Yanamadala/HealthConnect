package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appointment-service")
public interface AppointmentClient {

    @GetMapping("/api/appointments/count")
    Long getTotalAppointments();
}
