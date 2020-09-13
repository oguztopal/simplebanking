package com.assignment.simplebanking.model;

import com.assignment.simplebanking.type.TransactionType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WithdrawalTransaction extends Transaction{

  @Override
  public void setType(TransactionType transactionType){
    this.type = TransactionType.WithdrawalTransaction;
  }

  @Override
  public Double calculateBalance(Double amount){
    return getAccount().getBalance()-amount;
  }

  public WithdrawalTransaction(Double amount){
    super(amount);
  }
}
