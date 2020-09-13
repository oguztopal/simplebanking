package com.assignment.simplebanking.services;

import com.assignment.simplebanking.model.Account;
import com.assignment.simplebanking.model.dto.AccountDto;
import com.assignment.simplebanking.model.exception.InsufficientBalanceException;
import com.assignment.simplebanking.model.Transaction;
import com.assignment.simplebanking.controller.TransactionStatus;



public interface AccountService{

  /**
   * Get Account By Account Number
   *
   * @param accountNumber
   */
  Account findAccount(String accountNumber);

  /**
   * Deposit transaction for Account
   *
   * @param accountNumber
   * @param transaction
   */
  <T extends Transaction> TransactionStatus transaction(String accountNumber ,T transaction) throws InsufficientBalanceException;


  /**
   * create new Account
   *
   * @param account
   */
  void createAccount(AccountDto account);

}
