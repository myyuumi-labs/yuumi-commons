package com.yuumi.ecommerce.commons.exception;

public class BadRequestException extends RuntimeException {
	
    public BadRequestException(String desc) {
        super(desc);
    }

}
