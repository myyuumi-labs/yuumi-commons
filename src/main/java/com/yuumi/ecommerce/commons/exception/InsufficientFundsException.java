package com.yuumi.ecommerce.commons.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient Funds");
    }
}
