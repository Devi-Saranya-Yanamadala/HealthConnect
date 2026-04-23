package com.cts.healthconnect.appointment.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "slotbooking-service", url = "http://localhost:5001")
public interface SlotBookingClient {

    @PutMapping("/api/slots/book/{slotId}")
    void bookSlot(@PathVariable Long slotId);

    @PutMapping("/api/slots/release/{slotId}")
    void releaseSlot(@PathVariable Long slotId);
}
