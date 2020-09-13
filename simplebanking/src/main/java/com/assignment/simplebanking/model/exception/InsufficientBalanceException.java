package com.assignment.simplebanking.model.exception;

import com.assignment.simplebanking.type.ErrorCode;


public class InsufficientBalanceException extends Exception {

  private final ErrorCode code;

  private final String errorMessage;


  public InsufficientBalanceException(ErrorCode code, String message) {
    super(code != null ? "code:[" + code.name() + "] httpStatus:[" + code.getHttpStatus() + "]" : "");
    this.code = code;
    this.errorMessage = message;
  }

  public InsufficientBalanceException(Throwable cause, ErrorCode code, String message) {
    super(cause);
    this.code = code;
    this.errorMessage = message;
  }
  public ErrorCode getCode() {
    return this.code;
  }

  public String getErrorMessage() { return this.errorMessage; }
}
