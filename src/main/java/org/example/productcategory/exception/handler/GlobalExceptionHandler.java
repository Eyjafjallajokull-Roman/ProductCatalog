package org.example.productcategory.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.productcategory.exception.ApiException;
import org.example.productcategory.exception.BadRequestException;
import org.example.productcategory.exception.CategoryNotFoundException;
import org.example.productcategory.exception.CurrencyConversionException;
import org.example.productcategory.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiException> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
    return buildResponse(ex.getMessage(), req.getRequestURI(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ApiException> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest req) {
    return buildResponse(ex.getMessage(), req.getRequestURI(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ApiException> handleCategoryNotFound(CategoryNotFoundException ex, HttpServletRequest req) {
    return buildResponse(ex.getMessage(), req.getRequestURI(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiException> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
    return buildResponse(msg, req.getRequestURI(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiException> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
    return buildResponse(ex.getMessage(), req.getRequestURI(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiException> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
    return buildResponse("Method not allowed: " + ex.getMethod(), req.getRequestURI(), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiException> handleGeneric(Exception ex, HttpServletRequest req) {
    log.error("Unhandled exception", ex);
    return buildResponse("Internal server error", req.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(CurrencyConversionException.class)
  public ResponseEntity<ApiException> handleCurrencyConversionException(CurrencyConversionException ex, HttpServletRequest req) {
    return buildResponse("Currency conversion error: " + ex.getMessage(), req.getRequestURI(), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ApiException> buildResponse(String message, String path, HttpStatus status) {
    return ResponseEntity.status(status)
            .body(new ApiException(message, path, status.value(), LocalDateTime.now()));
  }
}