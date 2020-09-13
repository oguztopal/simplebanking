package com.assignment.simplebanking.model.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable{

  private String accountNumber;

  private String owner;

  private Double balance;

  private LocalDateTime createdAt;

  private Set<TransactionDto> transactions = new HashSet<>();

  public AccountDto(String accountNumber, String owner, Double balance){
    this.accountNumber = accountNumber;
    this.owner = owner;
    this.balance = balance;
  }
}
