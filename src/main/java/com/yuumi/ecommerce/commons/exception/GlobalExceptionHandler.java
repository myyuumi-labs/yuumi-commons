package com.yuumi.ecommerce.commons.exception;

import com.yuumi.ecommerce.commons.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
    return ApiResponse.asBadRequest(ex.getMessage());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException ex) {
    return ApiResponse.asForbidden("Forbidden", ex.getMessage());
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
    return ApiResponse.asConflict(ex.getMessage());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
    log.debug("AuthenticationException: {}", ex.getMessage());
    return ApiResponse.asUnauthorized(
        "The provided JWT token is invalid or expired",
        ex.getMessage()
    );
  }

  @ExceptionHandler(JwtAuthenticationException.class)
  public ResponseEntity<ApiResponse<Void>> handleJwtAuthenticationException(
      JwtAuthenticationException ex
  ) {
    log.debug("JwtAuthenticationException: {}", ex.getMessage());
    return ApiResponse.asUnauthorized(ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
    log.debug("AccessDeniedException: {}", ex.getMessage());
    return ApiResponse.asForbidden("Forbidden", ex.getMessage());
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleAccountNotFound(AccountNotFoundException ex) {
    return ApiResponse.asNotFound("The account ID does not exist", ex.getMessage());
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleCustomerNotFound(CustomerNotFoundException ex) {
    return ApiResponse.asNotFound("The customer ID does not exist", ex.getMessage());
  }

  @ExceptionHandler(ConsentNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleConsentNotFound(ConsentNotFoundException ex) {
    return ApiResponse.asNotFound("Consent Missing", ex.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
    return ApiResponse.asNotFound("Resource not found", ex.getMessage());
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ApiResponse<Void>> handleInsufficientFunds(InsufficientFundsException ex) {
    return ApiResponse.asUnprocessableEntity("Insufficient funds", ex.getMessage());
  }

  @ExceptionHandler(InvalidTransitionException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidTransition(InvalidTransitionException ex) {
    return ApiResponse.asBadRequest(ex.getMessage(), ex.getMessage());
  }

  @ExceptionHandler(VersionMismatchException.class)
  public ResponseEntity<ApiResponse<Void>> handleVersionMismatch(VersionMismatchException ex) {
    return ApiResponse.asConflict("Stale version or ETag", ex.getMessage());
  }

  @ExceptionHandler(UpstreamException.class)
  public ResponseEntity<ApiResponse<Void>> handleUpstream(UpstreamException ex) {
    return ApiResponse.asBadGateway(ex.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
    return ApiResponse.asBadRequest("Constraint violation", ex.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
    String detail = ex.getMostSpecificCause() == null
        ? ex.getMessage()
        : ex.getMostSpecificCause().getMessage();
    return ApiResponse.asBadRequest("Malformed JSON request", detail);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleFieldValidation(MethodArgumentNotValidException ex) {
    String firstMessage = "Input validation failed";
    String firstField = null;
    StringBuilder all = new StringBuilder();

    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      if (firstField == null) {
        firstField = fieldError.getField();
        if (fieldError.getDefaultMessage() != null) {
          firstMessage = fieldError.getDefaultMessage();
        }
      }
      if (!all.isEmpty()) {
        all.append("; ");
      }
      all.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage());
    }

    String details = firstField != null
        ? "field=" + firstField + " | " + all
        : all.toString();

    return ApiResponse.asBadRequest(firstMessage, details);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidation(ValidationException ex) {
    return ApiResponse.asBadRequest(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
    return ApiResponse.asBadRequest(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
    log.error("Unhandled exception", ex);
    return ApiResponse.asInternalError("Something went wrong", ex.getMessage());
  }
}
