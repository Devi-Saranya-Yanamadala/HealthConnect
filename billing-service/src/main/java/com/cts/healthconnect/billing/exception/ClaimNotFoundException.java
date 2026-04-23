package com.cts.healthconnect.billing.exception;

public class ClaimNotFoundException extends RuntimeException {
    public ClaimNotFoundException(String claimNumber) {
        super("Claim not found: " + claimNumber);
    }
}