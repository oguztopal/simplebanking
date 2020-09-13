package com.assignment.simplebanking.controller;

import com.assignment.simplebanking.model.Account;
import com.assignment.simplebanking.model.DepositTransaction;
import com.assignment.simplebanking.model.PhoneBillPaymentTransaction;
import com.assignment.simplebanking.model.exception.InsufficientBalanceException;
import com.assignment.simplebanking.model.WithdrawalTransaction;
import com.assignment.simplebanking.model.dto.AccountDto;
import com.assignment.simplebanking.services.AccountService;
import com.assignment.simplebanking.util.ApiPaths;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RequestMapping(ApiPaths.AccountApi.BASE)
@RestController
public class AccountController{

  private AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }


  @PostMapping(value = ApiPaths.AccountApi.CREATE_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> createAccount(@Valid @RequestBody AccountDto input) throws Exception{
    log.info("createAccount  started ");
    accountService.createAccount(input);
    log.info(" createAccount completed ");
    return ResponseEntity.ok(HttpStatus.OK);
  }


  @GetMapping(value = "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountDto> getAccount(@PathVariable(name = "accountNumber", required = true) String accountNumber) {
    ModelMapper modelMapper = new ModelMapper();
    log.debug("getByAccountNumber started accountNumber:{}", accountNumber);
    Account result = accountService.findAccount(accountNumber);
    log.debug("getByAccountNumber completed accountNumber:{} {}", accountNumber, result);
    AccountDto accountDto = modelMapper.map(result,AccountDto.class);
    accountDto.setAccountNumber(accountNumber);
    return ResponseEntity.ok(accountDto);
  }


  @PostMapping(value = ApiPaths.AccountApi.CREDIT + "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionStatus> deposit(@PathVariable(name = "accountNumber", required = true)  String accountNumber,@Valid @RequestBody DepositTransaction input)
      throws InsufficientBalanceException{
    log.info("deposit started accountNumber:{}", accountNumber);
    TransactionStatus transactionStatus = accountService.transaction(accountNumber,input);
    log.info("deposit completed accountNumber:{}", accountNumber);
    return ResponseEntity.ok(transactionStatus);
  }


  @PostMapping(value = ApiPaths.AccountApi.DEBIT + "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionStatus> credit(@PathVariable(name = "accountNumber", required = true) String accountNumber,@Valid @RequestBody WithdrawalTransaction input)
      throws InsufficientBalanceException{
    log.info("credit started accountNumber:{}", accountNumber);
    TransactionStatus transactionStatus = accountService.transaction(accountNumber,input);
    log.info("credit completed accountNumber:{}", accountNumber);
    return ResponseEntity.ok(transactionStatus);
  }

  @PostMapping(value = ApiPaths.AccountApi.PHONE_BILL_PAYMENT + "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionStatus> phoneBillPayment(@PathVariable(name = "accountNumber", required = true) String accountNumber,@Valid @RequestBody PhoneBillPaymentTransaction input)
      throws InsufficientBalanceException{
    log.info("phoneBillPayment started accountNumber:{}", accountNumber);
    TransactionStatus transactionStatus = accountService.transaction(accountNumber,input);
    log.info("phoneBillPayment completed accountNumber:{}", accountNumber);
    return ResponseEntity.ok(transactionStatus);
  }

}
