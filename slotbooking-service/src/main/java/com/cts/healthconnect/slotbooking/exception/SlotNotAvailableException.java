package com.cts.healthconnect.slotbooking.exception;


public class SlotNotAvailableException extends RuntimeException {

    public SlotNotAvailableException(Long slotId) {
        super("Slot " + slotId + " is not available");
    }
}
