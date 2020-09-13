package com.assignment.simplebanking.controller;

import com.assignment.simplebanking.model.exception.InsufficientBalanceException;
import com.assignment.simplebanking.model.response.TransactionErrorResponse;
import com.assignment.simplebanking.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityExistsException;


@ControllerAdvice
@Slf4j
public class ExceptionHandlerController{

  @ExceptionHandler({InsufficientBalanceException.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ResponseBody
  public TransactionErrorResponse handleInsufficientBalanceException(InsufficientBalanceException ex) {
    log.info("handleInsufficientBalanceException running");
    TransactionErrorResponse transactionErrorResponse = new TransactionErrorResponse(ex.getCode().getHttpStatus(),ex.getErrorMessage());
    log.info("handleInsufficientBalanceException complete");
    return transactionErrorResponse;
  }


  @ExceptionHandler({ EntityExistsException.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ResponseBody
  public TransactionErrorResponse handleInsufficientBalanceException(EntityExistsException ex) {
    log.info("entityExistsException running");
    TransactionErrorResponse transactionErrorResponse = new TransactionErrorResponse(HttpStatus.NOT_ACCEPTABLE,ex.getMessage());
    log.info("entityExistsException running");
    return transactionErrorResponse;
  }



}
