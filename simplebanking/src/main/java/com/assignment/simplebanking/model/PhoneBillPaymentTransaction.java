package com.assignment.simplebanking.model;

import com.assignment.simplebanking.type.BillType;
import com.assignment.simplebanking.type.PhoneOperator;
import com.assignment.simplebanking.type.TransactionType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PhoneBillPaymentTransaction extends BillPaymentTransaction{

  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private PhoneOperator phoneOperator;


  public PhoneBillPaymentTransaction(Long billId, BillType billType, String phoneNumber, PhoneOperator phoneOperator){
    super(billId, billType);
    this.phoneNumber = phoneNumber;
    this.phoneOperator = phoneOperator;
  }

  @Override
  public void setType(TransactionType transactionType){
    this.type = TransactionType.PhoneBillPayment;
  }

  @Override
  public void setBillType(BillType billType){
    this.billType = BillType.PhoneBillPayment;
  }
}
