package com.cts.healthconnect.billing.client;

import com.cts.healthconnect.billing.dto.AppointmentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "appointment-service", url = "http://localhost:5002")
public interface AppointmentClient {

    @GetMapping("/api/appointments/by-code/{appointmentCode}")
    AppointmentResponseDto getByCode(@PathVariable("appointmentCode") String appointmentCode);
}