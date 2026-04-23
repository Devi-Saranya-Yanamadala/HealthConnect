package com.cts.healthconnect.ward.exception;


public class BedNotAvailableException extends RuntimeException {

    public BedNotAvailableException(String bedNumber) {
        super("Bed not available: " + bedNumber);
    }
}


