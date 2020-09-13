package com.assignment.simplebanking;

import com.assignment.simplebanking.model.Account;
import com.assignment.simplebanking.model.DepositTransaction;
import com.assignment.simplebanking.model.Transaction;
import com.assignment.simplebanking.model.exception.InsufficientBalanceException;
import com.assignment.simplebanking.model.WithdrawalTransaction;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class ModelTest{

  Set<String> accountNumber = new HashSet<>();

  @Test
  public void testCreateAccount(){

    this.accountNumber.add("12345");

    Account account = new Account("Oguz Topal",createAccountNumber(this.accountNumber));
    assertEquals("Oguz Topal", account.getOwner());
    assertEquals(0, account.getBalance());
    assertEquals(accountNumber,account.getAccountNumber());
  }


  @Test
  public void testAccountTransaction() throws InsufficientBalanceException{
    this.accountNumber.add("12345");
    Account account = new Account("oguz topal",createAccountNumber(this.accountNumber));
    assertEquals(0,account.getTransactions().size());


    //Deposit Transaction
    Transaction depositTransaction =  new DepositTransaction(1000.000);
    depositTransaction.setAccount(account);
    account.insertTransaction(depositTransaction);
    assertEquals(depositTransaction.getAmount(),account.getBalance()); // account.getBalance() equals amount because balance starting with 0.0


    //WithDrawal Transaction
    Transaction withDrawalTransaction  = new WithdrawalTransaction(1000.0);
    withDrawalTransaction.setAccount(account);
    account.insertTransaction(withDrawalTransaction);
    assertEquals(0,account.getBalance());

    assertEquals(2, account.getTransactions().size());


  }

  @Test
  public void insufficientHandlerTest(){

    this.accountNumber.add("12345");

    Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
      Account account = new Account("Oguz Topal",this.accountNumber);
      Transaction depositT = new DepositTransaction(10.0);
      depositT.setAccount(account);
      account.insertTransaction(depositT);

      Transaction withDrawalT = new WithdrawalTransaction(11.0);
      withDrawalT.setAccount(account);
      account.insertTransaction(withDrawalT);
    });

    String exceptedMessage = "Balance not be negative";
    assertTrue(exceptedMessage.contains(((InsufficientBalanceException) exception).getErrorMessage()));


  }

  private Set<String> createAccountNumber(Set<String>  accountNumber){
    Set<String> newAccountNumber = new HashSet<>();
    for (String accountNumb : accountNumber){
      if (!accountNumb.equalsIgnoreCase("")){
        newAccountNumber.add(accountNumb);
      }
    }
    return newAccountNumber;
  }

}
