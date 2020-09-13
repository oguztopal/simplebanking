package com.assignment.simplebanking.type;

import org.springframework.http.HttpStatus;


public enum ErrorCode{


  ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND),



  AMOUNT_NOT_BE_NEGATIVE_OR_ZERO(HttpStatus.NOT_ACCEPTABLE),


  AMOUNT_NOT_BE_NULL(HttpStatus.NOT_ACCEPTABLE),


  INSUFFICIENT_BALANCE(HttpStatus.NOT_ACCEPTABLE);



  private HttpStatus httpStatus;

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  private ErrorCode(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
