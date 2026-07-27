package com.yuumi.ecommerce.commons.exception;
public class PreconditionRequiredException extends RuntimeException {
    public PreconditionRequiredException(String msg) { super(msg); }
}