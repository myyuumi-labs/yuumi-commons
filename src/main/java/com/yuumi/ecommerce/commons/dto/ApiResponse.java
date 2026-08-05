package com.yuumi.ecommerce.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.function.Consumer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private Integer code;
  private T data;

  @JsonProperty("page_metadata")
  private PageMetadata pageMetadata;

  private String message;
  private String details;

  public static <T> ResponseEntity<ApiResponse<T>> asSuccess(T data) {
    return response(200, builder -> builder.data(data));
  }

  public static <T> ResponseEntity<ApiResponse<List<T>>> asSuccess(
      List<T> data,
      PageMetadata pageMetadata
  ) {
    return response(200, builder -> builder.data(data).pageMetadata(pageMetadata));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asCreated(T data) {
    return response(201, builder -> builder.data(data));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asBadRequest(String message) {
    return response(400, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asBadRequest(String message, String details) {
    return response(400, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asUnauthorized(String message) {
    return response(401, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asUnauthorized(String message, String details) {
    return response(401, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asForbidden(String message) {
    return response(403, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asForbidden(String message, String details) {
    return response(403, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asNotFound(String message) {
    return response(404, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asNotFound(String message, String details) {
    return response(404, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asConflict(String message) {
    return response(409, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asConflict(String message, String details) {
    return response(409, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asUnprocessableEntity(String message) {
    return response(422, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asUnprocessableEntity(String message, String details) {
    return response(422, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asInternalError(String message) {
    return response(500, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asInternalError(String message, String details) {
    return response(500, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asBadGateway(String message) {
    return response(502, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> asBadGateway(String message, String details) {
    return response(502, builder -> builder.message(message).details(details));
  }

  public static <T> ResponseEntity<ApiResponse<T>> ofStatus(int status, String message) {
    return response(status, builder -> builder.message(message));
  }

  public static <T> ResponseEntity<ApiResponse<T>> ofStatus(int status, String message, String details) {
    return response(status, builder -> builder.message(message).details(details));
  }

  private static <T> ResponseEntity<ApiResponse<T>> response(
      int status,
      Consumer<ApiResponseBuilder<T>> enrich
  ) {
    ApiResponseBuilder<T> builder = ApiResponse.<T>builder().code(status);
    enrich.accept(builder);
    return ResponseEntity.status(status).body(builder.build());
  }
}
