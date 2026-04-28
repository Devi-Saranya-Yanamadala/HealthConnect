package com.cts.healthconnect.appointment.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.cts.healthconnect.appointment.config.FeignConfig;

@FeignClient(name = "slotbooking-service", url = "http://localhost:5001",configuration = FeignConfig.class)
public interface SlotBookingClient {

    @PutMapping("/api/slots/book/{slotId}")
    void bookSlot(@PathVariable Long slotId);

 
    

    @PatchMapping("/api/slots/{slotId}/release")
    void releaseSlot(@PathVariable Long slotId);

}
