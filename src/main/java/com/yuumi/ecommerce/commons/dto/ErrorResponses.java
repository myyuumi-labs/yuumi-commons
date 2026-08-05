package com.yuumi.ecommerce.commons.dto;

public final class ErrorResponses {

  private ErrorResponses() {
  }

  public static ErrorResponse of(String code, String message) {
    return of(code, message, message);
  }

  public static ErrorResponse of(String code, String message, String details) {
    return ErrorResponse.builder()
        .error(ErrorResponse.ErrorDetail.builder()
            .code(code)
            .message(message)
            .details(details)
            .build())
        .build();
  }
}
