package com.assignment.simplebanking.model;

import com.assignment.simplebanking.base.BaseEntity;
import com.assignment.simplebanking.model.exception.InsufficientBalanceException;
import com.assignment.simplebanking.type.ErrorCode;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account extends BaseEntity{

  @Id
  @SequenceGenerator(name = "account_seq_generator", sequenceName = "account_seq")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_generator")
  private Long id;

  @Column(name = "owner",nullable = false)
  private String owner;

  @ElementCollection
  @CollectionTable(name = "owner_account_numbers",
      joinColumns = @JoinColumn(name = "account_id"))
  @Column(name="account_number", nullable=false)
  private Set<String> accountNumber = new HashSet<>();

  @Column(name = "balance",nullable = false)
  @Min(value = 0,message = "its not be negative")
  private Double balance = 0.0;

  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
      cascade = { CascadeType.ALL }, mappedBy = "account")
  private Set<Transaction> transactions = new HashSet<>();

  public Account(Set<String> accountNumber,String owner, @Min(value = 0, message = "its not be negative") Double balance){
    this.accountNumber = accountNumber;
    this.owner = owner;
    this.balance = balance;
  }

  public Account(String owner, Set<String> accountNumber){
    this.owner = owner;
    this.accountNumber = accountNumber;
  }

  @Transient
  public <T extends Transaction> Set<Transaction> insertTransaction(T transaction) throws InsufficientBalanceException{
    Set<Transaction> transactionSet = new HashSet<>();
    transactionSet.add(transaction);
    this.balance = transaction.calculateBalance(transaction.getAmount());
    if (this.balance.compareTo(0.0) < 0) {
      throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE, "Balance not be negative");
    }
    return transactionSet;
  }
}
