package com.example.main.exceptions;

import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppError {
  private int status;
  private String message;
  private Date timestamp;

  public AppError(int status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = new Date();
  }
}
