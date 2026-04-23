package com.cts.healthconnect.slotbooking.exception;


public class SlotNotAvailableException extends RuntimeException {

    public SlotNotAvailableException(Long slotId) {
        super("Slot not available or already booked. Slot ID: " + slotId);
    }
}
