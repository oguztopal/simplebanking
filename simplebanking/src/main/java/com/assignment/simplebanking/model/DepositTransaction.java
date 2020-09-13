package com.assignment.simplebanking.model;

import com.assignment.simplebanking.type.TransactionType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public  class DepositTransaction extends Transaction{

  @Override
  public void setType(TransactionType transactionType){
     this.type = TransactionType.DepositTransaction;
  }

  @Override
  public Double calculateBalance(Double amount){
    return getAccount().getBalance()+amount;
  }

  public DepositTransaction(Double amount){
    super(amount);
  }

}
