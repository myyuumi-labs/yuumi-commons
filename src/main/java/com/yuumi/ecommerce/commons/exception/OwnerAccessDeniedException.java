package com.yuumi.ecommerce.commons.exception;

public class OwnerAccessDeniedException extends RuntimeException {
    public OwnerAccessDeniedException() {
        super("Invalid Owner " );
    }
}
