package com.yuumi.ecommerce.commons.exception;

public final class ErrorCodes {

  public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
  public static final String NOT_FOUND = "NOT_FOUND";
  public static final String FORBIDDEN = "FORBIDDEN";
  public static final String UNAUTHORIZED = "UNAUTHORIZED";
  public static final String CONFLICT = "CONFLICT";
  public static final String UPSTREAM_ERROR = "UPSTREAM_ERROR";
  public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

  public static final String TENANT_SUBDOMAIN_REQUIRED = "TENANT_SUBDOMAIN_REQUIRED";
  public static final String TENANT_NOT_FOUND = "TENANT_NOT_FOUND";
  public static final String TENANT_RESOLUTION_FAILED = "TENANT_RESOLUTION_FAILED";

  private ErrorCodes() {
  }
}
