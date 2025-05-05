package org.example.productcategory.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiException {
  private String message;
  private String path;
  private int status;
  private LocalDateTime timestamp;
}