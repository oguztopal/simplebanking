package com.assignment.simplebanking.model;

import com.assignment.simplebanking.type.BillType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
abstract class BillPaymentTransaction extends WithdrawalTransaction{


  private Long billId;

  @Enumerated(EnumType.STRING)
  protected BillType billType;

}
